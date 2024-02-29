package controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.CityDaoImpl;
import model.Registration;
import service.TokenValidationInterceptor;

@RestController
@RequestMapping(path = "/make")
public class PaymentGatewayController {

	private static final String MERCHANT_ID = "3119096";
    private static final String ACCESS_CODE = "AVNH05LB56CF25HNFC";   
    private static final String ENCRYPTION_KEY = "039AE11691FCF783D1539D35C6188AF9";
    private RestTemplate restTemplate; // Assuming RestTemplate is configured
    @Autowired
    public PaymentGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @RequestMapping(value = "/Payment", method = RequestMethod.POST)
    public String makePayment(HttpServletRequest request, @ModelAttribute PaymentForm paymentForm) {
        try {
            // Generate checksum
            String checksum = generateChecksum(paymentForm);
            // Encrypt the request parameters
            StringBuilder ccaRequestBuilder = new StringBuilder();
            ccaRequestBuilder.append("merchantId=").append(MERCHANT_ID).append("&");
            ccaRequestBuilder.append("accessCode=").append(ACCESS_CODE).append("&");
            ccaRequestBuilder.append("orderId=").append(paymentForm.getOrderId()).append("&");
            ccaRequestBuilder.append("amount=").append(paymentForm.getAmount()).append("&");
            // Add other required parameters
            ccaRequestBuilder.append("checksum=").append(checksum);
            // Encrypt the request parameters
            AesCryptUtil aesUtil = new AesCryptUtil(ENCRYPTION_KEY);
            String encRequest = aesUtil.encrypt(ccaRequestBuilder.toString());
            // Construct the URL for CCAvenue payment page
            String paymentUrl = "https://secure.ccavenue.com/transaction.do?command=initiateTransaction";
            String redirectUrl = paymentUrl + "&encRequest=" + URLEncoder.encode(encRequest, "UTF-8")
                    + "&access_code=" + ACCESS_CODE;
            // Redirect the user to the CCAvenue payment page
            return "redirect:" + redirectUrl;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error
            return "error"; // Redirect to an error page
        }
    }
    
    private String generateChecksum(PaymentForm paymentForm) throws NoSuchAlgorithmException {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("merchantId", MERCHANT_ID);
        params.put("orderId", paymentForm.getOrderId());
        params.put("amount", paymentForm.getAmount());
        // Add other required parameters
        StringBuilder paramsBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsBuilder.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
        }
        paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
        String data = paramsBuilder.toString() + ENCRYPTION_KEY;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data.getBytes());
        byte[] digest = md5.digest();
        StringBuilder checksumBuilder = new StringBuilder();
        for (byte b : digest) {
            checksumBuilder.append(String.format("%02x", b & 0xff));
        }
        return checksumBuilder.toString();
    }
	}

class PaymentForm {
    private String orderId;
    private String amount;
    // Add other fields

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    // Add getters and setters for other fields
}
