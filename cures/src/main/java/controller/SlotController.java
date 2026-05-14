package controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dto.ApiResponse;
import dto.SlotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.OtpService;
import service.SlotGeneratorService;
import service.SlotService;

@RestController
@RequestMapping("/slots")
public class SlotController {

    @Autowired
    private SlotService slotService;

    @Autowired
    private OtpService otpService;

    @GetMapping("/{doctorId}")
    public ResponseEntity<?> getSlots(
            @PathVariable Integer doctorId,
            @RequestParam Integer userId) {

        return ResponseEntity.ok(slotService.getSlots(doctorId, userId));
    }
    
 // 🔥 HOLD SLOT (user clicks slot)
    @PostMapping("/hold")
    public Map<String, Object> holdSlot(@RequestBody Map<String, String> req) {

        String slotStart = req.get("slotStart");
        Integer userId = Integer.valueOf(req.get("userId"));
        return slotService.holdSlot(slotStart, userId);
    }

    // 🔥 CONFIRM BOOKING AFTER PAYMENT
//    @PostMapping("/confirm")
//    public Map<String, String> confirm(@RequestBody Map<String, Object> req) {
//
//        Integer userId = (Integer) req.get("userId");
//        Long slotId = Long.valueOf(req.get("slotId").toString());
//
//        return slotService.confirmAppointment(userId, slotId);
//    }
    
 // 🔥 RELEASE SLOT (payment failed)
 //   @PostMapping("/release")
//    public String release(@RequestBody Map<String, Object> req) {
//
//        Long slotId = Long.valueOf(req.get("slotId").toString());
//
//        slotService.releaseSlot(slotId);
//
//        return "Released";
//    }

    @GetMapping
    public List<Map<String, Object>> getSlots(@RequestParam String date) {
        return slotService.getSlotsByDate(date);
    }

    @PostMapping("/lock-and-send-otp")
    public ResponseEntity<ApiResponse<Object>> lockAndSendOtp(@RequestBody SlotRequest req) {

        LocalDateTime time = LocalDateTime.parse(req.getAppointmentDateTime());

        // 🔒 already locked?
        if (slotService.isLocked(req.getDoctorId(), time)) {

            throw new RuntimeException("Slot unavailable");
        }

        // 🔥 CREATE UNIQUE LOCK ID
        String lockId = UUID.randomUUID().toString();

        try {

            // 🔥 SAVE LOCK
            slotService.lockSlot(lockId, req.getDoctorId(), time, req.getMobile());

            // 📱 SEND OTP
            otpService.sendOtp(req.getCountryCode(), req.getMobile());

        } catch (Exception e) {

            // ❌ REMOVE LOCK IF OTP FAILED
            slotService.releaseLock(lockId);
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP");
        }

        Map<String, Object> response = new HashMap<>();

        response.put("lockId", lockId);

        return ResponseEntity.ok(new ApiResponse<>(true, "Slot locked and OTP sent successfully", response));
    }
}