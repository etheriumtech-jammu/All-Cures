package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class DoctorPriorityManager {

    // doctorId → priority
    private final Map<Integer, Integer> priorityMap = new ConcurrentHashMap<>();

    // Get priority
    public int getPriority(Integer doctorId) {
        return priorityMap.getOrDefault(doctorId, 1); // default = 1
    }

    // Update single doctor
    public void updatePriority(Integer doctorId, int priority) {
        priorityMap.put(doctorId, priority);
    }

    // Bulk update
    public void updateBulk(Map<Integer, Integer> updates) {
        priorityMap.putAll(updates);
    }

    // Remove doctor (fallback to default)
    public void remove(Integer doctorId) {
        priorityMap.remove(doctorId);
    }

    // Get all priorities
    public Map<Integer, Integer> getAll() {
        return priorityMap;
    }

    // Clear all (reset)
    public void clearAll() {
        priorityMap.clear();
    }
}