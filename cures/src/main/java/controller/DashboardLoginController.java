package controller;

import dto.LoginRequest;
import model.DashboardUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard/login")
public class DashboardLoginController {

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        boolean valid = DashboardUser.isValid(request.getEmail(), request.getPassword());

        if (valid) {
            return ResponseEntity.ok("Login successful");
        }

        return ResponseEntity.status(401).body("Invalid email or password");
    }
}
