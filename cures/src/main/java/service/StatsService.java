package service;
import org.springframework.stereotype.Service;

import dao.StatsDao;
import java.time.LocalDate;
import java.math.BigInteger;
import java.util.*;

@Service
public class StatsService {

	  public Map<String, Object> getAppointmentsSummaryByStatus(int status) {
	        BigInteger count = StatsDao.getAppointmentsSummaryByStatus(status);

	        Map<String, Object> summary = new HashMap<>();
	        summary.put("totalAppointments", count);
	        return summary;
	    }
	  
    public List<Map<String, Object>> getMonthlyAppointments(int year) {
        List<Object[]> rawList = StatsDao.getMonthlyAppointments(year);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rawList) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("month", row[0]);
            entry.put("count", row[1]);
            result.add(entry);
        }

        return result;
    }
    
    public Map<String, Object> getAppointmentsCount(String startDate, String endDate) {
        BigInteger count = StatsDao.getAppointmentsCount(startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        result.put("totalAppointments", count);
        return result;
    }
    
    public Map<String, Object> listSucceededAppointments(
            String startDate, String endDate, Integer docId, int page, int size, Integer status) {
        return StatsDao.listSucceededAppointments(startDate, endDate, docId, page, size, status);
    }

	 
    public Map<String, Object> getDoctorsSummary(LocalDate startDate, LocalDate endDate) {
        BigInteger totalActive = StatsDao.countActiveDoctors(startDate, endDate);
        BigInteger totalSigned = StatsDao.countSignedDoctors(startDate, endDate);
        List<Map<String, Object>> byType = StatsDao.signedDoctorsByMedicineType(startDate, endDate);

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalActiveDoctors", totalActive);
        summary.put("totalSignedDoctors", totalSigned);
        summary.put("signedByMedicineType", byType);
        return summary;
    }

	 public Map<String, Object> getAppointmentStats(LocalDate startDate,
            LocalDate endDate) {
// DAO throws on error; we let it bubble up
return StatsDao.getAppointmentStats(startDate, endDate);
}
}
