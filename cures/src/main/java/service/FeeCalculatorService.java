package service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class FeeCalculatorService {

    private final FeeProperties feeProperties;    // injected via constructor

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public FeeCalculatorService(FeeProperties feeProperties) {
        this.feeProperties = feeProperties;
    }

    /**
     * Safely convert various types to BigDecimal.
     */
    public BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(SCALE, ROUNDING);
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(SCALE, ROUNDING);
        }

        if (value instanceof Number) {
            return new BigDecimal(value.toString()).setScale(SCALE, ROUNDING);
        }

        if (value instanceof String) {
            try {
                return new BigDecimal(((String) value).trim()).setScale(SCALE, ROUNDING);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Cannot parse numeric value from String: " + value, ex);
            }
        }

        throw new IllegalArgumentException("Unsupported fee type: " + value.getClass());
    }

    // Main method: base + gst + eth
    public BigDecimal calculateTotalFee(BigDecimal baseFee) {
        if (baseFee == null) baseFee = BigDecimal.ZERO.setScale(SCALE, ROUNDING);

        // debug: prints bound property values
        System.out.println("DEBUG gstRate  = " + feeProperties.getGstRate());
        System.out.println("DEBUG ethRate  = " + feeProperties.getEthereumRate());

        BigDecimal gst = baseFee.multiply(feeProperties.getGstRate());
        BigDecimal eth = baseFee.multiply(feeProperties.getEthereumRate());
        return baseFee.add(gst).add(eth).setScale(SCALE, ROUNDING);
    }

    // Full breakdown (optional)
    public Map<String, BigDecimal> buildBreakdown(BigDecimal baseFee) {
        if (baseFee == null) baseFee = BigDecimal.ZERO.setScale(SCALE, ROUNDING);

        BigDecimal gst = baseFee.multiply(feeProperties.getGstRate()).setScale(SCALE, ROUNDING);
        BigDecimal eth = baseFee.multiply(feeProperties.getEthereumRate()).setScale(SCALE, ROUNDING);
        BigDecimal total = baseFee.add(gst).add(eth).setScale(SCALE, ROUNDING);

        Map<String, BigDecimal> map = new HashMap<>();
  //      map.put("baseFee", baseFee.setScale(SCALE, ROUNDING));
        map.put("gst", gst);
        map.put("etheriumPart", eth);
        map.put("fee", total);

        return map;
    }
}
