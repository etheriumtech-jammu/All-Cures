package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dto.OtpRequest;
import service.OtpService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String mobile, @RequestParam String countryCode) {
        otpService.sendOtp(countryCode,mobile);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
    		@RequestBody OtpRequest request) {

        boolean valid = otpService.verifyOtp(request.getCountryCode(),request.getMobile(), request.getOtp());

        if (valid) {
            return ResponseEntity.ok("OTP Verified");
        }

        return ResponseEntity.badRequest().body("Invalid OTP");
    }
    
    @GetMapping("/check-otp")
    public void checkOtp(@RequestParam String mobile) {
		

		System.out.println("Checking OTP for mobile: " + mobile);
	}
}
