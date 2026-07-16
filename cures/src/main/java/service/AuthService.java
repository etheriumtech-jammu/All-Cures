// ===========================
// AuthService.java
// ===========================

package service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AuthDao;
import model.Registration;
import util.Constant;
import util.CookieManager;
import util.EnDeCryptor;

public class AuthService {

    public static Object registerUser(HashMap<String, Object> registerMap,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        Registration user = null;
        String errMsg = "";

        try {

        	String fullName = (String) registerMap.get(Constant.FIRSTNAME);

        	String firstName = "";
        	String lastName = "";

        	if (fullName != null && !fullName.trim().isEmpty()) {

        	    String[] parts = fullName.trim().split("\\s+");

        	    if (parts.length == 1) {

        	        firstName = parts[0];

        	    } else {

        	        lastName = parts[parts.length - 1];

        	        StringBuilder firstNameBuilder = new StringBuilder();

        	        for (int i = 0; i < parts.length - 1; i++) {

        	            if (i > 0) {
        	                firstNameBuilder.append(" ");
        	            }

        	            firstNameBuilder.append(parts[i]);
        	        }

        	        firstName = firstNameBuilder.toString();
        	    }
        	}
            // EMAIL OPTIONAL
            String email = registerMap.get(Constant.EMAIL) != null
                    ? ((String) registerMap.get(Constant.EMAIL)).trim()
                    : null;

            if (email != null && email.isEmpty()) {
                email = null;
            }
            if(email != null &&
            		   !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            		    return "Invalid email address";
            		}
            String password = (String) registerMap.get(Constant.PSW);


            String mobileString =
                    (String) registerMap.get(Constant.MOBILE_NUMBER);

            String countryCode =
                    (String) registerMap.get(Constant.COUNTRY_CODE);

            String docpatient =
                    (String) registerMap.get(Constant.DOCPATIENT);

            String remPwd =
                    registerMap.get(Constant.REMPWD) == null
                            ? Constant.OFF
                            : (String) registerMap.get(Constant.REMPWD);

            String acceptTnC =
                    (String) registerMap.get(Constant.AcceptTermsAndConditions);

            String acceptPolicy =
                    (String) registerMap.get(Constant.AcceptPolicy);

            // VALIDATIONS

            if (firstName == null || firstName.trim().isEmpty()) {
                return "First name is required";
            }

            if (mobileString == null || mobileString.trim().isEmpty()) {
                return "Mobile number is required";
            }

            if (password == null || password.trim().isEmpty()) {
                return "Password is required";
            }

            Long mobile = null;

            try {
                mobile = Long.parseLong(mobileString);
            } catch (Exception e) {
                return "Invalid mobile number";
            }

            Integer docOrPatient =
                    determineDocOrPatient(docpatient);

            Integer rememberPassword =
                    Constant.OFF.equalsIgnoreCase(remPwd.trim()) ? 0 : 1;

            Boolean accTerms =
                    Constant.ON.equalsIgnoreCase(acceptTnC);
            System.out.println("accTerms: " + accTerms);
            Boolean accPolicy =
                    Constant.ON.equalsIgnoreCase(acceptPolicy);

            Integer state = 1;

            Integer age = registerMap.get(Constant.Age) != null
                    ? (Integer) registerMap.get(Constant.Age)
                    : 0;

            // CHECK EXISTING USER
             if (mobileExists( mobile)) {

                return "Mobile number already exists";

            }

            if (emailExists(email )) {

                return "Email already exists";

            }

            // PASSWORD ENCRYPTION

//            BCryptPasswordEncoder encoder =
//                    new BCryptPasswordEncoder();
//
//            String hashedPassword =
//                    encoder.encode(password);
            EnDeCryptor encryptor = new EnDeCryptor();
            final String secretKey = Constant.SECRETE;		
    		String hashedPassword = encryptor.encrypt(password, secretKey);	
    		
            // SAVE USER

            user = AuthDao.saveRegistration(
                    firstName,
                    lastName,
                    hashedPassword,
                    email,
                    accTerms,
                    docOrPatient,
                    accPolicy,
                    state,
                    rememberPassword,
                    mobile,
                    age,
                    countryCode
            );

            if (user != null) {

                handleSuccessfulRegistration(
                        request,
                        response,
                        user,
                        rememberPassword
                );

            } else {

                return "Error while registering user";
            }

        } catch (Exception e) {

            e.printStackTrace();
            return "Internal Server Error";
        }

         
        return user;
    }

    // ===========================
    // CHECK USER EXISTS
    // ===========================

    private static boolean mobileExists(Long mobile) {

        if (mobile == null) {
            return false;
        }

        return AuthDao.getUserFromMobile(mobile) != null;
    }
    
    private static boolean emailExists(String email) {

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return AuthDao.getUserFromEmail(email) != null;
    }


    // ===========================
    // SESSION + COOKIES
    // ===========================

    private static void handleSuccessfulRegistration(
            HttpServletRequest request,
            HttpServletResponse response,
            Registration user,
            Integer rememberPassword) {

        request.getSession().setAttribute(Constant.USER, user);

        CookieManager cookieManager =
                new CookieManager();

        if (rememberPassword == 1) {

            cookieManager.dropAllCookies(response, user);

        } else {

            cookieManager.dropSessionCookies(response, user);
        }
    }

    // ===========================
    // DETERMINE USER TYPE
    // ===========================

    private static Integer determineDocOrPatient(
            String docpatient) {

        if ("doctor".equalsIgnoreCase(docpatient)) {
            return 1;
        }

        return 2;
    }
    
    
    public static Object loginUser(
            HashMap<String, Object> loginMap,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {

            String loginInput =
                    (String) loginMap.get("loginInput");

            String password =
                    (String) loginMap.get("password");

            String otp =
                    (String) loginMap.get("otp");

            String loginType =
                    (String) loginMap.get("loginType");

            String remPwd =
                    loginMap.get("rempwd") == null
                            ? Constant.OFF
                            : (String) loginMap.get("rempwd");

            if (loginInput == null ||
                    loginInput.trim().isEmpty()) {

                return "Email or Mobile is required";
            }

            Registration user = null;

            // ===========================
            // FIND USER
            // ===========================

            if (loginInput.contains("@")) {

                user = AuthDao.getUserFromEmail(
                        loginInput.trim()
                );

            } else {

                try {

                    Long mobile =
                            Long.parseLong(loginInput);

                    user =
                            AuthDao.getUserFromMobile(mobile);

                } catch (Exception e) {

                    return "Invalid Mobile Number";
                }
            }

            if (user == null) {

                return "User not found";
            }

            // ===========================
            // OTP LOGIN
            // ===========================

            if ("OTP".equalsIgnoreCase(loginType)) {

                if (otp == null || otp.trim().isEmpty()) {

                    return "OTP is required";
                }

                // VERIFY OTP HERE

                boolean otpValid = otp.equals("1234");

                if (!otpValid) {

                    return "Invalid OTP";
                }

            }

            // ===========================
            // PASSWORD LOGIN
            // ===========================

            else {

                if (password == null ||
                        password.trim().isEmpty()) {

                    return "Password is required";
                }

                EnDeCryptor decryptor =
                        new EnDeCryptor();

                final String secretKey =
                        Constant.SECRETE;

                String decryptedPassword =
                        decryptor.decrypt(
                                user.getPass_word(),
                                secretKey
                        );

                if (!password.equals(decryptedPassword)) {

                    return "Invalid Password";
                }
            }

            // ===========================
            // LOGIN SUCCESS
            // ===========================

            request.getSession()
                    .setAttribute(Constant.USER, user);

            CookieManager cookieManager =
                    new CookieManager();

            Integer rememberPassword =
                    Constant.OFF.equalsIgnoreCase(
                            remPwd.trim()
                    ) ? 0 : 1;

            if (rememberPassword == 1) {

                cookieManager.dropAllCookies(
                        response,
                        user
                );

            } else {

                cookieManager.dropSessionCookies(
                        response,
                        user
                );
            }

            return user;

        } catch (Exception e) {

            e.printStackTrace();

            return "Internal Server Error";
        }
    }
    
   
}
