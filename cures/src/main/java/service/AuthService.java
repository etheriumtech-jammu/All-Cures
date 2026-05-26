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

public class AuthService {

    public static Object registerUser(HashMap<String, Object> registerMap,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        Registration user = null;
        String errMsg = "";

        try {

            String firstName = (String) registerMap.get(Constant.FIRSTNAME);
            String lastName = (String) registerMap.get(Constant.LASTNAME);

            // EMAIL OPTIONAL
            String email = registerMap.get(Constant.EMAIL) != null
                    ? ((String) registerMap.get(Constant.EMAIL)).trim()
                    : null;

            if (email != null && email.isEmpty()) {
                email = null;
            }

            String password = (String) registerMap.get(Constant.PSW);

            String confirmPassword =
                    (String) registerMap.get(Constant.PSWREPEAT);

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

            if (!password.equals(confirmPassword)) {
                return "Password and Confirm Password do not match";
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

            Boolean accPolicy =
                    Constant.ON.equalsIgnoreCase(acceptPolicy);

            Integer state = 1;

            Integer age = registerMap.get(Constant.Age) != null
                    ? (Integer) registerMap.get(Constant.Age)
                    : 0;

            // CHECK EXISTING USER

            if (alreadyExists(email, mobile)) {

                return "Mobile number or email already exists";

            }

            // PASSWORD ENCRYPTION

            BCryptPasswordEncoder encoder =
                    new BCryptPasswordEncoder();

            String hashedPassword =
                    encoder.encode(password);

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
                System.out.println("User registered successfully: " + user.getRegistration_id());
                System.out.println(user.getRegistration_type());
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

    private static boolean alreadyExists(String email,
                                         Long mobile) {

        Registration mobileUser =
                AuthDao.getUserFromMobile(mobile);

        if (mobileUser != null) {
            return true;
        }

        if (email != null && !email.trim().isEmpty()) {

            Registration emailUser =
                    AuthDao.getUserFromEmail(email);

            if (emailUser != null) {
                return true;
            }
        }

        return false;
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
}
