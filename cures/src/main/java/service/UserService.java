package service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dto.OtpRequest;
import dto.RegisterRequest;
import model.Registration;

public interface UserService {

    Registration findByMobile(Long mobile);

    Registration createOtpUser(OtpRequest request);

    Registration registerWithPassword(RegisterRequest request);

	boolean existsByMobile(Long mobile);
    void handleCookies(HttpServletRequest request, HttpServletResponse response,
			Registration user, Integer rememberPassword);

	Registration validatePasswordLogin(Long mobile, String password);


}
