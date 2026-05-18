package config;


import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class CcavenueConfig {

	@Value("${ccavenue.workingKey}")
    private String workingKey;

    @Value("${ccavenue.accessCode}")
    private String accessCode;

    @Value("${ccavenue.merchantId}")
    private String merchantId;

    @Value("${payment.baseAmount}")
    private String baseAmount;

   
    public String getWorkingKey() {
        return workingKey;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getBaseAmount() {
        return baseAmount;
    }
}