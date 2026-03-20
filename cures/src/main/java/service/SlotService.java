package service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.PaymentGatewayDaoImpl;

import repository.DoctorSlotRepository;
import repository.FeeRepository;
import util.HibernateUtil;

@Service
public class SlotService {

    @Autowired
    private DoctorSlotRepository slotRepo;

    @Autowired
    private DoctorPriorityManager priorityManager;

    @Autowired
    private AuditService auditService;
    @Autowired
    private FeeRepository feeRepo;

    @Autowired
    private FeeCalculatorService feeCalculatorService;

 //   @Cacheable(value = "slots", key = "#doctorId + '_' + #userId")
    public Map<String, Object> getSlots(Integer doctorId, Integer userId) {

        Map<String, Object> response = new HashMap<>();

        Map<LocalDate, Set<LocalTime>> availableDates = new TreeMap<>();
        Map<LocalDate, Set<LocalTime>> unbookedSlots = new TreeMap<>();
        List<LocalDate> completelyBookedDates = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(30);

        List<Object[]> slotData = slotRepo.getSlotsInRange(doctorId, today, end);

        Map<LocalDate, List<LocalDateTime>> slotMap = new HashMap<>();
        Map<LocalDate, Integer> totalMap = new HashMap<>();
        Map<LocalDate, Integer> bookedMap = new HashMap<>();

        for (Object[] row : slotData) {
            LocalDateTime dt = ((java.sql.Timestamp) row[0]).toLocalDateTime();
            Number bookedVal = (Number) row[1];
            boolean isBooked = bookedVal != null && bookedVal.intValue() == 1;

            LocalDate date = dt.toLocalDate();

            if (!isBooked) {
                slotMap.computeIfAbsent(date, k -> new ArrayList<>()).add(dt);
            }

            totalMap.put(date, totalMap.getOrDefault(date, 0) + 1);

            if (isBooked) {
                bookedMap.put(date, bookedMap.getOrDefault(date, 0) + 1);
            }
        }

        for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {

            List<LocalDateTime> slots = slotMap.getOrDefault(date, new ArrayList<>());

            TreeSet<LocalTime> times = new TreeSet<>();

            for (LocalDateTime dt : slots) {
                times.add(dt.toLocalTime());
            }

            availableDates.put(date, times);
            unbookedSlots.put(date, times);

            int total = totalMap.getOrDefault(date, 0);
            int booked = bookedMap.getOrDefault(date, 0);

            if (total > 0 && total == booked) {
                completelyBookedDates.add(date);
            }
        }

        // ✅ Reusable fee logic
        response.putAll(getFeeResponse(doctorId, userId));

        response.put("totalDates", availableDates);
        response.put("unbookedSlots", unbookedSlots);
        response.put("completelyBookedDates", completelyBookedDates);

        return response;
    }
    
    
    public Map<String, Object> holdSlot(String slotStartStr, Integer userId) {

        Session session = null;
        Transaction tx = null;

        Map<String, Object> res = new HashMap<>();

        try {
            session = HibernateUtil.buildSessionFactory();
            tx = session.beginTransaction();

            // ✅ Validate input
            if (slotStartStr == null || userId == null) {
                throw new IllegalArgumentException("slotStartStr or userId is null");
            }

            LocalDateTime slotStart = LocalDateTime.parse(slotStartStr);
            
            System.out.println("Attempting to hold slot at " + slotStart + " for user " + userId);
            
            // 🔥 STEP 1: Fetch slots (LOCKED)
            List<Object[]> slots = session.createNativeQuery(
                    "SELECT ds.slotId, ds.doctor_id, ds.end_datetime " +
                    "FROM doctor_slots ds " +
                    "WHERE ds.start_datetime = :start " +
                    "AND (ds.is_booked = 0 OR ds.is_booked IS NULL)" +
                    "AND (ds.hold_until IS NULL OR ds.hold_until < NOW()) " +
                    "FOR UPDATE")
            		.setParameter("start", slotStart) 
                    .getResultList();
            
            if (slots == null || slots.isEmpty()) {
                throw new RuntimeException("No slots available for this time");
            }

            // 🔥 STEP 2: Appointment count
            List<Object[]> counts = session.createNativeQuery(
                    "SELECT docID, COUNT(*) " +
                    "FROM Appointment " +
                    "WHERE DATE(appointmentDate) = CURDATE() " +
                    "GROUP BY docID")
                    .getResultList();

            Map<Integer, Integer> countMap = new HashMap<>();
            for (Object[] row : counts) {
                countMap.put(
                    ((Number) row[0]).intValue(),
                    ((Number) row[1]).intValue()
                );
            }

            // 🔥 STEP 3: Sort by priority
            slots.sort((a, b) -> {
                int docA = ((Number) a[1]).intValue();
                int docB = ((Number) b[1]).intValue();

                int pA = priorityManager.getPriority(docA);
                int pB = priorityManager.getPriority(docB);

                return Integer.compare(pA, pB);
            });

            Object[] selected = null;
            int MAX_APPOINTMENTS = 2;

            // 🔥 STEP 4: Apply rule
            for (Object[] slot : slots) {
                Integer doctorId = ((Number) slot[1]).intValue();
                int totalAppointments = countMap.getOrDefault(doctorId, 0);

                if (totalAppointments < MAX_APPOINTMENTS) {
                    selected = slot;
                    break;
                }
            }

            if (selected == null) {
                throw new RuntimeException("All doctors reached max limit for today");
            }

            // 🔥 STEP 5: HOLD SLOT
            Long slotId = ((Number) selected[0]).longValue();
            Integer doctorId = ((Number) selected[1]).intValue();
            LocalDateTime endTime =
                    ((java.sql.Timestamp) selected[2]).toLocalDateTime();

            int updated = session.createNativeQuery(
                    "UPDATE doctor_slots " +
                    "SET hold_until = NOW() + INTERVAL 5 MINUTE " +
                    "WHERE slotId = :slotId")
                    .setParameter("slotId", slotId)
                    .executeUpdate();

            if (updated == 0) {
                throw new RuntimeException("Failed to hold slot");
            }

            // ✅ Commit DB before external call (IMPORTANT)
            tx.commit();

            // 🔥 AUDIT SUCCESS
//            auditService.log(session, null, doctorId, slotId,
//                    "HOLD_SLOT", "SUCCESS", "Slot held");

            Map<String, Object> feeInfo = getFeeResponse(doctorId, userId);
            // 🔥 PAYMENT CALL (separate try block)
            try {
                HashMap<String, Object> paymentMap = new HashMap<>();
                paymentMap.put("userID", userId);
                paymentMap.put("docID", doctorId);
                paymentMap.put("slotId", slotId);
                if(feeInfo.containsKey("feeError")) {
					throw new RuntimeException("Fee info error: " + feeInfo.get("feeError"));
				}
                if(!feeInfo.containsKey("amount") || feeInfo.get("amount") == null) {
               					throw new RuntimeException("Fee info error: amount missing");
               					}
                Map<String, Object> amountMap = (Map<String, Object>) feeInfo.get("amount");

                if (amountMap == null || !amountMap.containsKey("totalFee") || amountMap.get("totalFee") == null) {
                    throw new RuntimeException("Fee info error: total amount missing");
                }

                BigDecimal totalAmount = new BigDecimal(amountMap.get("totalFee").toString());
                paymentMap.put("amount", totalAmount);
                paymentMap.put("currency",feeInfo.getOrDefault("currency_symbol", "₹ "));
                System.out.println("Initiating payment with data: " + paymentMap);
                HashMap<String, String> payRes =
                        PaymentGatewayDaoImpl.setSlotPayment(paymentMap);

                if (payRes != null) {
                    res.putAll(payRes);
                }

                res.put("paymentStatus", "INITIATED");

            } catch (Exception paymentEx) {
                // ⚠️ Do NOT rollback slot hold
                res.put("paymentStatus", "FAILED");
                res.put("paymentError", paymentEx.getMessage());
            }

            // ✅ FINAL RESPONSE
            res.put("slotId", slotId);
            res.put("doctorId", doctorId);
            res.put("endTime", endTime.toString());
            res.put("status", "HELD");
            res.put("accessCode", "AVWN42KL59BP42NWPB");
            res.putAll(feeInfo);
            return res;

        } catch (IllegalArgumentException e) {

            if (tx != null && tx.isActive()) tx.rollback();

            res.put("error", "INVALID_INPUT");
            res.put("message", e.getMessage());

        } catch (RuntimeException e) {

            if (tx != null && tx.isActive()) tx.rollback();

            res.put("error", "BUSINESS_ERROR");
            res.put("message", e.getMessage());

        } catch (Exception e) {

            if (tx != null && tx.isActive()) tx.rollback();

            res.put("error", "SYSTEM_ERROR");
            res.put("message", "Something went wrong");

            e.printStackTrace(); // keep for debugging

        } 

        return res;
    }
    
    public List<Map<String, Object>> getSlotsByDate(String dateStr) {

        Session session = HibernateUtil.buildSessionFactory();

        LocalDate requestedDate = LocalDate.parse(dateStr);
        LocalDate today = LocalDate.now();

        String sql;

        // 🔥 If today → filter past slots
        if (requestedDate.equals(today)) {

            sql = "SELECT TIME(start_datetime), COUNT(*), " +
                  "SUM(CASE WHEN is_booked = 1 THEN 1 ELSE 0 END) " +
                  "FROM doctor_slots ds " +
                  "WHERE DATE(start_datetime) = :date " +
                  "AND start_datetime >= NOW() " +   // 🔥 KEY LINE
                  "AND (ds.hold_until IS NULL OR ds.hold_until < NOW()) " +
                  "GROUP BY TIME(start_datetime)" +
                  "ORDER BY TIME(start_datetime) ASC"; 

        } else {

            // 🔥 Future date → no filter
            sql = "SELECT TIME(start_datetime), COUNT(*), " +
                  "SUM(CASE WHEN is_booked = 1 THEN 1 ELSE 0 END) " +
                  "FROM doctor_slots ds " +
                  "WHERE DATE(start_datetime) = :date " +
                  "AND (ds.hold_until IS NULL OR ds.hold_until < NOW()) " +
                  "GROUP BY TIME(start_datetime)" +
                  "ORDER BY TIME(start_datetime) ASC"; 
        }

        List<Object[]> rows = session.createNativeQuery(sql)
                .setParameter("date", dateStr)
                .getResultList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rows) {

            String time = row[0].toString();
            int total = ((Number) row[1]).intValue();
            int booked = row[2] != null ? ((Number) row[2]).intValue() : 0;

            int available = total - booked;

            // 🔥 Optional: skip fully booked slots
            if (available <= 0) continue;

            Map<String, Object> map = new TreeMap<>();
            map.put("time", time);
            map.put("available", available);

            result.add(map);
        }
        

        return result;
    }
    
    private Map<String, Object> getFeeResponse(Integer doctorId, Integer userId) {

        Map<String, Object> feeResponse = new HashMap<>();

        try {
            Object[] feeRow = feeRepo.getFeeDetails(doctorId, userId);

            BigDecimal baseFee = feeCalculatorService.toBigDecimal(feeRow[0]);
            String countryCode = (String) feeRow[1];
            String currencySymbol = (String) feeRow[2];

            if (countryCode == null || "IN".equalsIgnoreCase(countryCode)) {

                BigDecimal totalFee = feeCalculatorService.calculateTotalFee(baseFee);

                Map<String, BigDecimal> breakdown =
                        feeCalculatorService.buildBreakdown(totalFee);

                feeResponse.put("amount", breakdown);
                feeResponse.put("currency_symbol", "₹ ");

            } else {
                feeResponse.put("amount", "0");
                feeResponse.put("currency_symbol", currencySymbol + " ");
            }

        } catch (Exception e) {
            feeResponse.put("feeError", "Unable to fetch fee");
        }

        return feeResponse;
    }
}