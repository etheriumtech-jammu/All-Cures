package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Component
//@ConfigurationProperties(prefix = "app.fees")
public class FeeProperties {

//	  @Value("${app.fees.gst-rate:}")
    private BigDecimal gstRate=  BigDecimal.valueOf(0.18);
	  
//	  @Value("${app.fees.ethereum-rate:}")
    private BigDecimal ethereumRate=BigDecimal.valueOf(0.10);

    public BigDecimal getGstRate() {
        return gstRate;
    }
    public void setGstRate(BigDecimal gstRate) {
        this.gstRate = gstRate;
    }

    public BigDecimal getEthereumRate() {
        return ethereumRate;
    }
    public void setEthereumRate(BigDecimal ethereumRate) {
        this.ethereumRate = ethereumRate;
    }

    @PostConstruct
    public void validate() {
        if (gstRate == null) {
            throw new IllegalStateException("Missing required property: app.fees.gst-rate");
        }
        if (ethereumRate == null) {
            throw new IllegalStateException("Missing required property: app.fees.ethereum-rate");
        }
    }

    @Override
    public String toString() {
        return "FeeProperties{gstRate=" + gstRate + ", ethereumRate=" + ethereumRate + "}";
    }
}
