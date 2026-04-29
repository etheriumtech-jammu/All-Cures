package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.OtpException;
import util.OtpUtil;

@Service
public class OtpService {

    @Autowired
    private OtpStorageService storageService;

    @Autowired
    private WhatsAppService whatsAppService;

    public void sendOtp(String countryCode,String mobile) {

        try {
            String existingOtp = storageService.getOtp(countryCode,mobile);

            String otp = (existingOtp != null)
                    ? existingOtp
                    : OtpUtil.generateOtp();

            storageService.saveOtp(countryCode,mobile, otp);

            boolean sent = whatsAppService.sendOtp(countryCode,mobile, otp);

            if (!sent) {
                throw new OtpException("Failed to send OTP via WhatsApp");
            }

        } catch (OtpException e) {
            throw e;
        } catch (Exception e) {
            throw new OtpException("Error while sending OTP", e);
        }
    }

    public boolean verifyOtp(String countryCode,String mobile, String inputOtp) {

        try {
            String storedOtp = storageService.getOtp(countryCode,mobile);

            if (storedOtp == null) {
                throw new OtpException("OTP expired or not found");
            }

            if (!storedOtp.equals(inputOtp)) {
                throw new OtpException("Invalid OTP");
            }

  //          storageService.deleteOtp(countryCode,mobile);
            storageService.markVerified(countryCode,mobile,inputOtp);

            return true;

        } catch (OtpException e) {
            throw e;
        } catch (Exception e) {
            throw new OtpException("Error while verifying OTP", e);
        }
    }
}
