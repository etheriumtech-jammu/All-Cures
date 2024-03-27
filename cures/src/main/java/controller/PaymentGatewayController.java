package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import util.AesCryptUtil;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
@RestController
@RequestMapping(path = "/make")
public class PaymentGatewayController {

	private static final String MERCHANT_ID = "3119096";
    private static final String ACCESS_CODE = "AVKI05LC59AW25IKWA";   
    private static final String ENCRYPTION_KEY = "80923CFC322F5875BA18A25A84B3F05B";
    private static final String COMMAND="confirmOrder";
    private static final String Request_Type="XML";
    private static final String Response_Type="XML";
    private static final String Version="1.1";
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
	    ccaRequestBuilder.append("reference_no=").append(paymentForm.getReferenceNo()).append("&");
            
            // Add other required parameters
            ccaRequestBuilder.append("checksum=").append(checksum);
            // Encrypt the request parameters
            AesCryptUtil aesUtil = new AesCryptUtil(ENCRYPTION_KEY);
            String encRequest = aesUtil.encrypt(ccaRequestBuilder.toString());
	    System.out.println("encRequest"+encRequest);
            // Construct the URL for CCAvenue payment page
            String paymentUrl = "https://test.ccavenue.com/transaction.do?command=initiateTransaction";
            String redirectUrl = paymentUrl + "&encRequest=" + URLEncoder.encode(encRequest, "UTF-8")
                    + "&access_code=" + ACCESS_CODE  + "&command=" + COMMAND  + "&request_type=" + Request_Type
                    + "&response_type=" + Response_Type + "&version=" + Version;
	    System.out.println("redirectUrl"+redirectUrl);
            // Redirect the user to the CCAvenue payment page
            return "redirect:" + redirectUrl;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error
            return "error"; // Redirect to an error page
        }
    }

	@RequestMapping(value = "/ccavenue-payment-udpates", method = RequestMethod.POST)
    public String PaymentUpdates(HttpServletRequest request, HashMap<String, Object> PaymentMap) {
		System.out.println("PaymentMap"+ PaymentMap);
	    return "Success";
    }
    private String generateChecksum(PaymentForm paymentForm) throws NoSuchAlgorithmException {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("merchantId", MERCHANT_ID);
        params.put("orderId", paymentForm.getOrderId());
        params.put("amount", paymentForm.getAmount());
	  params.put("reference_no", paymentForm.getReferenceNo());
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
    private String referenceNo;

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

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
}
