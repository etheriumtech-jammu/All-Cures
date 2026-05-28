// ===============================
// LoginService.java
// ===============================

package service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.LoginDao;
import model.Registration;
import util.Constant;
import util.CookieManager;
import util.Encryption;

public class LoginService {

    private LoginDao loginDao =
            new LoginDao();

    public void login(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType(
                "application/json"
        );

        PrintWriter out =
                response.getWriter();

        try {

            // =====================================
            // REQUEST PARAMETERS
            // =====================================

            String loginType =
                    request.getParameter(
                            "loginType"
                    );

            String email =
                    request.getParameter(
                            "email"
                    );

            String mobileStr =
                    request.getParameter(
                            "mobile"
                    );

            String password =
                    request.getParameter(
                            "password"
                    );

            String otp =
                    request.getParameter(
                            "otp"
                    );

            String rememberMe =
                    request.getParameter(
                            "rempwd"
                    );

            Registration user = null;

            // =====================================
            // PASSWORD ENCRYPTION
            // =====================================

            Encryption encryption =
                    new Encryption();

            String encryptedPassword =
                    null;

            if (password != null &&
                    !password.trim().isEmpty()) {

                encryptedPassword =
                        encryption.encrypt(
                                password,
                                Constant.SECRETE
                        );
            }

            // =====================================
            // EMAIL LOGIN
            // =====================================

            if ("EMAIL".equalsIgnoreCase(
                    loginType)) {

                if (email == null ||
                        email.trim().isEmpty()) {

                    response.setStatus(400);

                    out.write(
                            "Email is required"
                    );

                    return;
                }

                user =
                        loginDao.loginWithEmail(
                                email,
                                encryptedPassword
                        );
            }

            // =====================================
            // MOBILE LOGIN
            // =====================================

            else if ("MOBILE"
                    .equalsIgnoreCase(
                            loginType)) {

                if (mobileStr == null ||
                        mobileStr.trim().isEmpty()) {

                    response.setStatus(400);

                    out.write(
                            "Mobile is required"
                    );

                    return;
                }

                Long mobile =
                        Long.parseLong(
                                mobileStr
                        );

                user =
                        loginDao.loginWithMobile(
                                mobile,
                                encryptedPassword
                        );
            }

            // =====================================
            // OTP LOGIN
            // =====================================

            else if ("OTP"
                    .equalsIgnoreCase(
                            loginType)) {

                if (mobileStr == null ||
                        mobileStr.trim().isEmpty()) {

                    response.setStatus(400);

                    out.write(
                            "Mobile is required"
                    );

                    return;
                }

                if (otp == null ||
                        otp.trim().isEmpty()) {

                    response.setStatus(400);

                    out.write(
                            "OTP is required"
                    );

                    return;
                }

                Long mobile =
                        Long.parseLong(
                                mobileStr
                        );

                user =
                        loginDao.loginWithOtp(
                                mobile,
                                otp
                        );
            }

            else {

                response.setStatus(400);

                out.write(
                        "Invalid Login Type"
                );

                return;
            }

            // =====================================
            // INVALID LOGIN
            // =====================================

            if (user == null) {

                response.setStatus(401);

                out.write(
                        "Invalid Credentials"
                );

                return;
            }

            // =====================================
            // SESSION CREATION
            // =====================================

            HttpSession session =
                    request.getSession(
                            true
                    );

            session.setAttribute(
                    Constant.USER,
                    user
            );

            session.setAttribute(
                    "userId",
                    user.getRegistration_id()
            );


            // =====================================
            // COOKIE CREATION
            // =====================================

            CookieManager cookieManager =
                    new CookieManager();

            if ("1".equals(
                    rememberMe)) {

                cookieManager
                        .dropAllCookies(
                                response,
                                user
                        );

            } else {

                cookieManager
                        .dropSessionCookies(
                                response,
                                user
                        );
            }

            // =====================================
            // SUCCESS RESPONSE
            // =====================================

            Gson gson =
                    new GsonBuilder()
                            .serializeNulls()
                            .create();

            response.setStatus(
                    200
            );

            out.write(
                    gson.toJson(
                            user
                    )
            );
        }

        // =====================================
        // INVALID MOBILE FORMAT
        // =====================================

        catch (NumberFormatException e) {

            response.setStatus(
                    400
            );

            out.write(
                    "Invalid Mobile Number"
            );

            e.printStackTrace();
        }

        // =====================================
        // GENERAL EXCEPTION
        // =====================================

        catch (Exception e) {

            response.setStatus(
                    500
            );

            out.write(
                    "Internal Server Error"
            );

            e.printStackTrace();
        }


    }}