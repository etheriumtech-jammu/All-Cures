package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

// import exception.OtpException;
import util.WAPICommon;

@Service
public class WhatsAppService {

//      public boolean sendOtp(String countryCode,Long mobile, String otp) {
//
//        try {
//            Properties prop = new WAPICommon().readPropertiesFile("whatsapi.properties");
//
//            final String POST_PARAMS = "{"
//                    + "\"countryCode\": \"" + countryCode + "\","
//                    + "\"phoneNumber\": \"" + mobile + "\","
//                    + "\"type\": \"Template\","
//                    + "\"template\": {"
//                    + "\"name\": \"otp_verification_v1\","
//                    + "\"languageCode\": \"en\","
//                    + "\"bodyValues\": [\"" + otp + "\"],"
//                    + "\"buttonValues\": {"
//                    + "\"0\": [\"" + otp + "\"]"
//                    + "}"
//                    + "}"
//                    + "}";
//            System.setProperty("javax.net.ssl.trustStoreType", "jks");
// 	       System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/certs/java/cacerts");
// 	        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//
//            URL obj = new URL(prop.getProperty("URL_API_TEMPLATES"));
//            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
//
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", "Basic " + prop.getProperty("Authorization_Key"));
//            conn.setRequestProperty("Content-Type", "application/json");
//
//            conn.setDoOutput(true);
//            OutputStream os = conn.getOutputStream();
//            os.write(POST_PARAMS.getBytes());
//            os.flush();
//            os.close();
//
//            int responseCode = conn.getResponseCode();
//
//            BufferedReader reader;
//
//            if (responseCode >= 200 && responseCode < 300) {
//                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            String line;
//            StringBuilder response = new StringBuilder();
//
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//
//            reader.close();
//
//            System.out.println("OTP Response Code: " + responseCode);
//            System.out.println("OTP Response Body: " + response.toString());
//
//            if (responseCode >= 200 && responseCode < 300) {
//                return true;
//            }
//
//            throw new OtpException("WhatsApp API failed with code: " + responseCode);
//
//        } catch (OtpException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new OtpException("Error calling WhatsApp API", e);
//        }
//    }
      
      
      public boolean sendAdminPaymentAlert(
    	        String orderId
    	) {

    	    try {

    	        Properties prop =
    	                new WAPICommon()
    	                        .readPropertiesFile(
    	                                "whatsapi.properties"
    	                        );

    	        // 🔥 Admin Numbers
    	        String admin1 = "7006268978";
    	        String admin2 = "9654746673";

    	        // 🔥 Send to both admins
    	        sendTemplate(
    	                prop,
    	                admin1,
    	                orderId
    	        );

    	        sendTemplate(
    	                prop,
    	                admin2,
    	                orderId
    	        );

    	        return true;

    	    } catch (Exception e) {

    	        e.printStackTrace();

    	        return false;
    	    }
    	}
      
      private void sendTemplate(
    	        Properties prop,
    	        String mobile,
    	        String orderId
    	) throws Exception {

    	    final String POST_PARAMS =
    	            "{"
    	                    + "\"countryCode\": \"+91\","

    	                    + "\"phoneNumber\": \""
    	                    + mobile
    	                    + "\","

    	                    + "\"type\": \"Template\","

    	                    + "\"template\": {"

    	                    + "\"name\": \"payment_success_alert\","

    	                    + "\"languageCode\": \"en\","

    	                    + "\"bodyValues\": ["

    	                    + "\""
    	                    + orderId
    	                    + "\""

    	                    + "]"

    	                    + "}"

    	                    + "}";

    	    System.setProperty(
    	            "javax.net.ssl.trustStoreType",
    	            "jks"
    	    );

    	    System.setProperty(
    	            "javax.net.ssl.trustStore",
    	            "/etc/ssl/certs/java/cacerts"
    	    );

    	    System.setProperty(
    	            "javax.net.ssl.trustStorePassword",
    	            "changeit"
    	    );

    	    URL obj =
    	            new URL(
    	                    prop.getProperty(
    	                            "URL_API_TEMPLATES"
    	                    )
    	            );

    	    HttpsURLConnection conn =
    	            (HttpsURLConnection)
    	                    obj.openConnection();

    	    conn.setRequestMethod("POST");

    	    conn.setRequestProperty(
    	            "Authorization",
    	            "Basic "
    	                    + prop.getProperty(
    	                    "Authorization_Key"
    	            )
    	    );

    	    conn.setRequestProperty(
    	            "Content-Type",
    	            "application/json"
    	    );

    	    conn.setDoOutput(true);

    	    OutputStream os =
    	            conn.getOutputStream();

    	    os.write(POST_PARAMS.getBytes());

    	    os.flush();

    	    os.close();

    	    int responseCode =
    	            conn.getResponseCode();

    	    BufferedReader reader;

    	    if (responseCode >= 200 &&
    	            responseCode < 300) {

    	        reader =
    	                new BufferedReader(
    	                        new InputStreamReader(
    	                                conn.getInputStream()
    	                        )
    	                );

    	    } else {

    	        reader =
    	                new BufferedReader(
    	                        new InputStreamReader(
    	                                conn.getErrorStream()
    	                        )
    	                );
    	    }

    	    String line;

    	    StringBuilder response =
    	            new StringBuilder();

    	    while ((line = reader.readLine()) != null) {

    	        response.append(line);
    	    }

    	    reader.close();

    	    System.out.println(
    	            "Payment Alert Response Code: "
    	                    + responseCode
    	    );

    	    System.out.println(
    	            "Payment Alert Response Body: "
    	                    + response.toString()
    	    );
    	}
}
