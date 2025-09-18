package util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class PaymentUtil {

    private static final String workingKey = "0C8A93B072D45A598061718B364E36B5"; // Ideally inject this
//     private static final String workingKey = "039AE11691FCF783D1539D35C6188AF9";
    public static Map<String, String> decryptResponse(HttpServletRequest request) {
        String encResp = request.getParameter("encResp");
        AesCryptUtil aesUtil = new AesCryptUtil(workingKey);
        String decResp = aesUtil.decrypt(encResp);

        Map<String, String> paramMap = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(decResp, "&");

        while (tokenizer.hasMoreTokens()) {
            String pair = tokenizer.nextToken();
            if (pair != null) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2) {
                    paramMap.put(parts[0], parts[1]);
                } else if (parts.length == 1) {
                    paramMap.put(parts[0], "");
                }
            }
        }
        return paramMap;
    }
}
