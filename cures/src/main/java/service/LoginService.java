package service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.LoginDao;

import model.Registration;
import util.Constant;
import util.CookieManager;
import util.Encryption;

public class LoginService {

    private LoginDao loginDao = new LoginDao();

    public void login(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String loginType =
                request.getParameter("loginType");

        String email =
                request.getParameter("email");

        Long mobile =
                Long.parseLong(
                        request.getParameter("mobile"));


        String password =
                request.getParameter("password");

        String otp =
                request.getParameter("otp");

        String rememberMe =
                request.getParameter("rempwd");

        PrintWriter out = response.getWriter();

        Registration user = null;

        // PASSWORD ENCRYPTION
        Encryption encryption = new Encryption();

        String encryptedPassword = null;

        if(password != null && !password.isEmpty()) {

            encryptedPassword =
                    encryption.encrypt(
                            password,
                            Constant.SECRETE
                    );
        }

        // =============================
        // EMAIL LOGIN
        // =============================

        if("EMAIL".equalsIgnoreCase(loginType)) {

            user = loginDao.loginWithEmail(
                    email,
                    encryptedPassword
            );
        }

        // =============================
        // MOBILE LOGIN
        // =============================

        else if("MOBILE".equalsIgnoreCase(loginType)) {

            user = loginDao.loginWithMobile(
                    mobile,
                    encryptedPassword
            );
        }

        // =============================
        // OTP LOGIN
        // =============================

        else if("OTP".equalsIgnoreCase(loginType)) {

            user = loginDao.loginWithOtp(
                    mobile,
                    otp
            );
        }

        // =============================
        // INVALID LOGIN
        // =============================

        if(user == null) {

            response.setStatus(401);

            out.write("Invalid Credentials");

            out.flush();

            return;
        }

        // =============================
        // SESSION CREATION
        // =============================

        HttpSession session =
                request.getSession(true);

        session.setAttribute(
                Constant.USER,
                user
        );

        session.setAttribute(
                "userId",
                user.getRegistration_id()
        );

        // =============================
        // JWT TOKEN
        // =============================

        String token =
                JWTTokenValidationInterceptor
                        .generateJWTToken(
                                user.getEmail_address()
                        );

        user.setValue(token);

        // =============================
        // COOKIES
        // =============================

        CookieManager cookieManager =
                new CookieManager();

        if("1".equals(rememberMe)) {

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

        // =============================
        // SUCCESS RESPONSE
        // =============================

        Gson gson =
                new GsonBuilder()
                        .serializeNulls()
                        .create();

        response.setContentType(
                "application/json"
        );

        response.setStatus(200);

        out.write(
                gson.toJson(user)
        );

        out.flush();
    }
}