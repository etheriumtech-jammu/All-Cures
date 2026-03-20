package controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import service.AdminPriorityService;

@RestController
@RequestMapping("/admin/priority")
public class AdminPriorityController {

    @Autowired
    private AdminPriorityService service;

    // ✅ Update single doctor priority
    @PostMapping("/update")
    public String updatePriority(@RequestBody Map<String, Object> req) {

        Integer doctorId = (Integer) req.get("doctorId");
        Integer priority = (Integer) req.get("priority");

        return service.updatePriority(doctorId, priority);
    }

    // ✅ Bulk update
    @PostMapping("/bulk")
    public String bulkUpdate(@RequestBody Map<Integer, Integer> map) {
        return service.bulkUpdate(map);
    }

    // ✅ Get all priorities
    @GetMapping("/all")
    public Map<Integer, Integer> getAll() {
        return service.getAll();
    }

    // ✅ Remove one
    @DeleteMapping("/remove/{doctorId}")
    public String remove(@PathVariable Integer doctorId) {
        return service.remove(doctorId);
    }

    // ✅ Clear all
    @DeleteMapping("/clear")
    public String clearAll() {
        return service.clearAll();
    }
}