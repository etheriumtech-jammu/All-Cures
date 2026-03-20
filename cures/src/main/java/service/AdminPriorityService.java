package service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminPriorityService {

    @Autowired
    private DoctorPriorityManager priorityManager;

    public String updatePriority(Integer doctorId, int priority) {
        priorityManager.updatePriority(doctorId, priority);
        return "Priority updated for doctor " + doctorId;
    }

    public String bulkUpdate(Map<Integer, Integer> map) {
        priorityManager.updateBulk(map);
        return "Bulk priority updated";
    }

    public Map<Integer, Integer> getAll() {
        return priorityManager.getAll();
    }

    public String remove(Integer doctorId) {
        priorityManager.remove(doctorId);
        return "Priority removed for doctor " + doctorId;
    }

    public String clearAll() {
        priorityManager.clearAll();
        return "All priorities cleared";
    }
}