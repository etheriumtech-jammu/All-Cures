package util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimplifiedRatioCalculator {
	
	  List<Integer> values = new ArrayList<>();
	  List<String> brands = new ArrayList<>();
	 public static LinkedHashMap<String, Double> CalculateRatio(LinkedHashMap<String, Integer> brandAdCounts )
	 {
		 LinkedHashMap<String,Double> results= calculateSimplifiedRatio(brandAdCounts);
	        System.out.println("Simplified Ratio: " + results);
	        return results;
	 }
    public static void main(String[] args) {
      
        LinkedHashMap<String, Integer> brandAdCounts = new LinkedHashMap<>();       
  //      values.add(115);
 //       values.add(45);
 //       values.add(75);

//        String simplifiedRatio = calculateSimplifiedRatio(brandAdCounts);
//        System.out.println("Simplified Ratio: " + simplifiedRatio);
    }

    private static int findGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return findGCD(b, a % b);
    }

    public static LinkedHashMap calculateSimplifiedRatio( LinkedHashMap<String, Integer> brandAdCounts) {
    	LinkedHashMap<String, Double> Results = new LinkedHashMap<>();
//    	System.out.println("brandAdCounts"+brandAdCounts);
    	List<Integer> values = new ArrayList<>();
  	  List<String> brands = new ArrayList<>();
    	for (Map.Entry<String, Integer> entry : brandAdCounts.entrySet()) {
     //       System.out.println(brand);
    		String brand=entry.getKey();
    		brands.add(brand);
    		Results.put(brand, null);
    		
            Integer count = entry.getValue();
            values.add(count);
    	}
        int gcd = values.get(0);
        for (int value : values) {
            gcd = findGCD(gcd, value);
        }

        StringBuilder ratioBuilder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
        	 int ratioValue = values.get(i) / gcd;
            double decimalRatioValue = (double) ratioValue / 10.0; // Divide by 10.0 to get the decimal ratio
          
            Results.put(brands.get(i), decimalRatioValue);
  //          System.out.println(decimalRatioValue);
            
            ratioBuilder.append(decimalRatioValue).append(":");
        }
        ratioBuilder.setLength(ratioBuilder.length() - 1); // Remove the trailing colon
//        System.out.println("Results"+Results);
 //       return ratioBuilder.toString();
        return Results;
    }
}
