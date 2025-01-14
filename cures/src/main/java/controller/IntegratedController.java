package controller;

import dao.IntegratedDao;
import model.Doctors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IntegratedController {

    private final IntegratedDao integratedDao;

    public IntegratedController(IntegratedDao integratedDao) {
        this.integratedDao = integratedDao;
    }

    /**
     * Search for doctors by name prefix.
     * 
     * @param name The search prefix (at least 3 characters)
     * @return List of doctors matching the prefix
     */
    @RequestMapping(value = "/doctors/search", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody ArrayList<String> searchDoctors(@RequestParam String name) {
        return integratedDao.searchUsers(name);
    }
    
    @GetMapping(value = "/city/search", produces = "application/json")
    public @ResponseBody List<String> searchCities(@RequestParam String query) {
        List<String> results = integratedDao.searchCities(query);

        // ✅ Ensure JSON Response is consistent (not returning null)
        if (results == null || results.isEmpty()) {
            return List.of();  // Returns empty JSON array `[]`
        }

        return results;  // ✅ JSON will be returned automatically
    }
}
