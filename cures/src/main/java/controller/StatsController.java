package controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import service.StatsService;

import java.util.*;

@RestController
@RequestMapping("/stats/appointments")
public class StatsController {

    @Autowired
    private StatsService statsService;
    // ✅ Successful appointments summary
    @GetMapping("/summary/success")
    public Map<String, Object> getAppointmentsSummarySuccess() {
        return statsService.getAppointmentsSummaryByStatus(4); // Status=4 → success
    }

    // ✅ Failed appointments summary
    @GetMapping("/summary/failed")
    public Map<String, Object> getAppointmentsSummaryFailed() {
        return statsService.getAppointmentsSummaryByStatus(3); // Status=3 → failed
    }
    @GetMapping("/count")
    public Map<String, Object> getAppointmentsCount(
            @RequestParam String startDate,
            @RequestParam(required = false) String endDate) {

        // if endDate not provided, default to today
        if (endDate == null || endDate.isEmpty()) {
            endDate = java.time.LocalDate.now().toString(); // yyyy-MM-dd
        }

        return statsService.getAppointmentsCount(startDate, endDate);
    }

    
    @GetMapping("/success/list")
    public Map<String, Object> getAppointments(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer docId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "25") int limit,
            @RequestParam(required = false) Integer status) {
        // if endDate not provided, default to today
        if (endDate == null || endDate.isEmpty()) {
            endDate = java.time.LocalDate.now().toString(); // yyyy-MM-dd
        }

        return statsService.listSucceededAppointments(startDate, endDate,docId,limit,offset,status);
    }
}
