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
        BigDecimal eth = baseFee.multiply(feeProperties.getEthereumRate());
        System.out.println("eth:" + eth);
        BigDecimal gst = eth.multiply(feeProperties.getGstRate());
        System.out.println("gst on eth:" + gst);
        return baseFee.add(eth).add(gst).setScale(SCALE, ROUNDING);

    }

 // inside FeeCalculatorService
    public Map<String, BigDecimal> buildBreakdown(BigDecimal totalFee) {
        if (totalFee == null) {
            totalFee = BigDecimal.ZERO.setScale(SCALE, ROUNDING);
        } else {
            totalFee = totalFee.setScale(SCALE, ROUNDING);
        }

        BigDecimal gstRate = feeProperties.getGstRate() == null
                ? BigDecimal.ZERO
                : feeProperties.getGstRate().setScale(SCALE + 4, ROUNDING); // extra precision while calculating
        BigDecimal ethRate = feeProperties.getEthereumRate() == null
                ? BigDecimal.ZERO
                : feeProperties.getEthereumRate().setScale(SCALE + 4, ROUNDING);

        // divisor = 1 + gstRate + ethRate
        BigDecimal divisor = BigDecimal.ONE
                .add(ethRate)
                .add(ethRate.multiply(gstRate));

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            // defensive: avoid division by zero (shouldn't happen if rates are sensible)
            throw new IllegalStateException("Sum of rates must not be -1.0");
        }

        // base = total / (1 + gstRate + ethRate)
        BigDecimal baseFee = totalFee.divide(divisor, SCALE + 4, ROUNDING).setScale(SCALE, ROUNDING);

        
        // compute parts from base

        BigDecimal ethereumPart = baseFee.multiply(ethRate).setScale(SCALE, ROUNDING);
        BigDecimal gst = ethereumPart.multiply(gstRate).setScale(SCALE, ROUNDING);
        

        // recomposed total (useful to check rounding effects)
        BigDecimal recomposedTotal = baseFee.add(ethereumPart).add(gst)
                .setScale(SCALE, ROUNDING);

        Map<String, BigDecimal> map = new HashMap<>();
        map.put("baseFee", baseFee);
        map.put("gst", gst);
        map.put("etheriumPart", ethereumPart);
        map.put("totalFee", recomposedTotal);

        return map;
    }

}
