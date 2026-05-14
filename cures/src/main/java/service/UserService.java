package service;

import dto.OtpRequest;
import dto.RegisterRequest;
import model.Registration;

public interface UserService {

    Registration findByMobile(Long mobile);

    Registration createOtpUser(OtpRequest request);

    Registration registerWithPassword(RegisterRequest request);

	boolean existsByMobile(Long mobile);

	Registration validatePasswordLogin(Long mobile, String password);


}