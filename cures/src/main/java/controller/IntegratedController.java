package controller;

import dao.IntegratedDao;
import model.Doctors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
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
    @RequestMapping(value = "/search", produces = "application/json", method = RequestMethod.GET)
    public @ResponseBody ArrayList<String> searchDoctors(@RequestParam String name) {
        return integratedDao.searchUsers(name);
    }
}

