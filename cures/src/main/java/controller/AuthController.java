package controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import dao.PaymentGatewayDaoImpl;
import dao.RegistrationDaoImpl;
import dao.RegistrationDaoImpl_New;
import dto.ApiResponse;
import dto.OtpRequest;
import dto.RegisterRequest;
import exception.OtpException;
import model.Appointment;
import model.Registration;
import model.SlotLock;
import service.AppointmentService;
import service.AuthService;
import service.OtpService;
import service.SlotService;
import service.UserService;
import util.Constant;
import util.CookieManager;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private SlotService slotService;
    
    @Autowired
    private AppointmentService appointmentService;

    // =========================================
    // 📱 SEND OTP
    // =========================================
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(
            @RequestParam Long mobile,
            @RequestParam String countryCode,
            @RequestParam String purpose) {

        Registration user =
                userService.findByMobile(mobile);

        // ===========================
        // REGISTER FLOW
        // ===========================
        if ("REGISTER".equalsIgnoreCase(purpose)) {

            if (user != null) {
                throw new RuntimeException(
                        "Mobile number already registered"
                );
            }
        }

        // ===========================
        // LOGIN FLOW
        // ===========================
        else if ("LOGIN".equalsIgnoreCase(purpose)) {

            if (user == null) {
                throw new RuntimeException(
                        "No account found with this mobile number"
                );
            }
        }

        otpService.sendOtp(countryCode, mobile);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "OTP sent"));
    }
    // =========================================
    // 🔐 VERIFY OTP
    // =========================================
    
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(
            @RequestBody OtpRequest req,
            HttpServletRequest request,
            HttpServletResponse httpresponse) {
    	SlotLock lock = null;
    		try {
        // =========================================
        // 🔐 VERIFY OTP
        // =========================================
        boolean valid =
                otpService.verifyOtp(
                        req.getCountryCode(),
                        req.getMobile(),
                        req.getOtp()
                );

        
        if (!valid) {
            throw new OtpException("Invalid OTP");
        }
       
        // =========================================
        // 🔍 FIND / CREATE USER
        // =========================================
        Registration user =
                userService.findByMobile(req.getMobile());

     // ======================================================
     // ⭐ NEW : REGISTRATION OTP FLOW
     // ======================================================
     if ("REGISTER".equalsIgnoreCase(req.getPurpose())) {

         if (user != null) {
             throw new RuntimeException(
                     "Mobile number already registered"
             );
         }
         // NEW
         otpService.consumeOtp(
                 req.getCountryCode(),
                 req.getMobile(),
				 req.getOtp()
         );

         // ⭐ NEW
         return ResponseEntity.ok(
                 new ApiResponse<>(
                         true,
                         "OTP verified successfully"
                 )
         );
     }
     
  // ======================================================
  // ⭐ LOGIN FLOW CONTINUES
  // ======================================================
  if (user == null) {
      throw new RuntimeException(
              "Account not found"
      );
  }

        userService.handleCookies(
                request,
                httpresponse,
                user,
                req.getRememberPassword()
        );
        // =========================================
        // 🔥 LOGIN SESSION
        // =========================================
        HttpSession session =
                request.getSession();

        session.setAttribute(Constant.USER, user);
       
        // =========================================
        // 🔥 NORMAL LOGIN FLOW
        // =========================================
        if (req.getLockId() == null) {
        	 otpService.consumeOtp(
     				req.getCountryCode(),
     				req.getMobile(),
     				req.getOtp()
     		);
        	
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Login successful",
                            user
                    )
            );
        }

        // =========================================
        // 🔒 VALIDATE LOCK
        // =========================================
       lock =
                slotService.validateLock(
                        req.getLockId(),
                        req.getMobile()
                );

        // =========================================
        // 🔥 CREATE PENDING APPOINTMENT
        // =========================================
        Appointment appointment =
                appointmentService.createPendingAppointment(
                        user.getRegistration_id(),
                        lock.getDoctorId(),
                        lock.getAppointmentTime(),
                        req.getAmount()
                );

       
        // =========================================
        // 💳 GENERATE CC AVENUE DATA
        // =========================================
        Map<String, Object> response = new HashMap<>();
        Map<String, String> res = new HashMap<>();

        // Prepare appointment map for payment gateway
        HashMap<String, Object> appointmentMap = new HashMap<>();
        appointmentMap.put("currency", "INR");
        appointmentMap.put("amount", appointment.getAmount());

        // Generate payment request
        HashMap<String, String> payRes =
                PaymentGatewayDaoImpl.setPayment(
                        appointmentMap,
                        appointment.getAppointmentID()
                );

     // =========================================
     // ❌ PAYMENT INIT FAILED
     // =========================================
     if (payRes == null || payRes.isEmpty()) {

         // optional
         appointmentService.cancelAppointment(
                 appointment.getAppointmentID()
         );

         throw new RuntimeException(
                 "Unable to initiate payment"
         );
     }

     // =========================================
     // ✅ NOW CONSUME OTP
     // =========================================
     otpService.consumeOtp(
             req.getCountryCode(),
             req.getMobile(),
             req.getOtp()
     );

     // =========================================
     // ✅ NOW MARK LOCK USED
     // =========================================
     slotService.markLockUsed(lock);

     // =========================================
     // 🔥 SUCCESS RESPONSE
     // =========================================
     res.putAll(payRes);
       
        // Add access code
        res.put("accessCode", "AVWN42KL59BP42NWPB");

        // Final response
        response.put("paymentData", res);
        response.put("appointmentId",
                appointment.getAppointmentID());
       
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Proceed to payment",
                        response
                )
        );
        
    		}
    		catch (Exception e) {

    	        // =========================================
    	        // 🔥 RELEASE LOCK ON FAILURE
    	        // =========================================
    	        if (lock != null) {

    	            try {
    	                slotService.releaseLock(lock.getLockId());
    	            } catch (Exception ex) {
    	                ex.printStackTrace();
    	            }
    	        }

    	        throw new RuntimeException(
    	                e.getMessage()
    	        );
    	    }
    }

    // =========================================
    // 🧾 REGISTER
    // =========================================
    @PostMapping("/register-mobile")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest req,
    		 HttpServletRequest request,
             javax.servlet.http.HttpServletResponse responseHttp) {

        Registration user = userService.registerWithPassword(req);
        userService.handleCookies(
				request,
				responseHttp,
				user,
				req.getRememberPassword()
		);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "User registered successfully", user.getRegistration_id())
        );
    }

    // =========================================
    // 🔐 LOGIN
    // =========================================

    @PostMapping("/login-mobile")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody RegisterRequest req,
                                                     HttpServletRequest request,
                                                     javax.servlet.http.HttpServletResponse responseHttp) {

        Registration user = userService.validatePasswordLogin(
                req.getMobile(),
                req.getPassword()
        );
        userService.handleCookies(
				request,
				responseHttp,
				user,
				req.getRememberPassword()
		);
        HttpSession session = request.getSession();
        session.setAttribute(Constant.USER, user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", user));
    }

    // =========================================
    // 🔍 ME
    // =========================================
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> getLoggedInUser(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(Constant.USER) == null) {
            throw new RuntimeException("Not logged in");
        }

        Registration user = (Registration) session.getAttribute(Constant.USER);

        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched", user));
    }
    
    
    @RequestMapping(value = "/register-user", method = RequestMethod.POST)
    @ResponseBody
    public Object registerUser(@RequestBody HashMap<String, Object> registerMap,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        return AuthService.registerUser(registerMap, request, response);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object loginUser(
            @RequestBody HashMap<String, Object> loginMap,
            HttpServletRequest request,
            HttpServletResponse response) {

        return AuthService.loginUser(
                loginMap,
                request,
                response
        );
    }
}
