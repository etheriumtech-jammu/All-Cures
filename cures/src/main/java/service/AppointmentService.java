package service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import model.Appointment;

import util.HibernateUtil;

@Service
@Transactional
public class  AppointmentService {

	private static FeeCalculatorService feeCalculatorService;

   Session session;
   
   public AppointmentService(FeeCalculatorService feeCalculatorService) {
		this.feeCalculatorService = feeCalculatorService;
	}
   
    // =========================================
    // 🔥 CREATE PENDING APPOINTMENT
    // =========================================
  
    public Appointment createPendingAppointment(
            int userId,
            int doctorId,
            LocalDateTime appointmentTime,
            Double frontendAmount) {

        try {

            // =========================================
            // 💰 FETCH CONSULTATION FEE
            // =========================================
        	session=HibernateUtil.buildSessionFactory();
            Object[] row =
                    (Object[]) session
                            .createNativeQuery(

                                    "SELECT " +
                                    "cc.currency_name, " +

                                    "(SELECT sc.Fee " +
                                    "FROM ServiceContractDetails sc " +
                                    "WHERE sc.UserID = ( " +

                                    "SELECT r2.registration_id " +
                                    "FROM registration r2 " +
                                    "WHERE r2.DocID = :docId " +
                                    "LIMIT 1 ) " +

                                    "LIMIT 1 ) AS Fee " +

                                    "FROM registration r " +

                                    "LEFT JOIN countries_currencies cc " +
                                    "ON UPPER(cc.country_code) = " +
                                    "UPPER(r.country_code) " +

                                    "WHERE r.registration_id = :userId"

                            )
                            .setParameter("userId", userId)
                            .setParameter("docId", doctorId)
                            .uniqueResult();

            if (row == null || row[1] == null) {

                throw new RuntimeException(
                        "Consultation fee not found"
                );
            }

            // =========================================
            // 💵 BACKEND FEE
            // =========================================

            BigDecimal consultationFee =
                    new BigDecimal(
                            row[1].toString()
                    );

            // =========================================
            // 🔥 FINAL FEE CALCULATION
            // =========================================

            BigDecimal finalAmount =
                    feeCalculatorService
                            .calculateTotalFee(
                                    consultationFee
                            );
            System.out.println("Backend Calculated Amount: " + finalAmount);
            // =========================================
            // 🔒 PAYMENT TAMPERING CHECK
            // =========================================

            if (frontendAmount == null ||
                finalAmount.doubleValue()
                        != frontendAmount) {

                throw new RuntimeException(
                        "Amount mismatch detected"
                );
            }

            // =========================================
            // 🔥 CREATE APPOINTMENT
            // =========================================

            Appointment appointment =
                    new Appointment();

            appointment.setUserID(userId);

            appointment.setDocID(doctorId);

            // =========================================
            // 📅 DATE + TIME
            // =========================================

            appointment.setAppointmentDate(
                    java.sql.Date.valueOf(
                            appointmentTime.toLocalDate()
                    )
            );

            appointment.setStartTime(
                    appointmentTime
                            .toLocalTime()
                            .toString()
            );

            appointment.setEndTime(
					appointmentTime
							.plusMinutes(15)
							.toLocalTime()
							.toString()
			);
            
            appointment.setAmount(finalAmount);
            // =========================================
            // 🔥 STATUS
            // =========================================

            // 0 = PENDING
            appointment.setStatus(0);

            // 0 = unpaid
            appointment.setPaymentStatus(0);
     

            // =========================================
            // 💾 SAVE
            // =========================================

            session.save(appointment);

            return appointment;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    e.getMessage()
            );
        }
    }

    // =========================================
    // ✅ PAYMENT SUCCESS
    // =========================================
   
    public void confirmAppointmentPayment(
            String orderId,
            String paymentId) {

        Appointment appointment =
                session
                        .createQuery(
                                "FROM Appointment " +
                                "WHERE orderID=:orderId",
                                Appointment.class
                        )
                        .setParameter(
                                "orderId",
                                orderId
                        )
                        .uniqueResult();

        if (appointment == null) {

            throw new RuntimeException(
                    "Appointment not found"
            );
        }

        // 🔥 SUCCESS
        appointment.setStatus(1);

        appointment.setPaymentStatus(1);

        session.update(appointment);
    }

    // =========================================
    // ❌ PAYMENT FAILED
    // =========================================
    
    public void failAppointmentPayment(
            String orderId) {

    	session=HibernateUtil.buildSessionFactory();
        Appointment appointment =
                session
                        .createQuery(
                                "FROM Appointment " +
                                "WHERE orderID=:orderId",
                                Appointment.class
                        )
                        .setParameter(
                                "orderId",
                                orderId
                        )
                        .uniqueResult();

        if (appointment == null) {

            throw new RuntimeException(
                    "Appointment not found"
            );
        }

        // 🔥 FAILED
        appointment.setStatus(2);

        appointment.setPaymentStatus(2);

        session.update(appointment);
    }

	public void cancelAppointment(int appointmentID) {
		// TODO Auto-generated method stub
		session=HibernateUtil.buildSessionFactory();
		Appointment appointment =
				session
						.createQuery(
								"FROM Appointment " +
								"WHERE appointmentID=:appointmentID",
								Appointment.class
						)
						.setParameter(
								"appointmentID",
								appointmentID
						)
						.uniqueResult();

		if (appointment == null) {

			throw new RuntimeException(
					"Appointment not found"
			);
		}

		// 🔥 FAILED
		appointment.setStatus(5);

		appointment.setPaymentStatus(0);

		session.update(appointment);	
		
	}
}