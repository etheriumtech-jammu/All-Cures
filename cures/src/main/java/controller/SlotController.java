package controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.SlotGeneratorService;
import service.SlotService;

@RestController
@RequestMapping("/slots")
public class SlotController {

    @Autowired
    private SlotService slotService;
    

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
}