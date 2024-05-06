package util;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWT {

 //   private static final SecretKey SECRET_KEY = generateKey();

	 static String encodedString = "oPOIO20yKmeBdPlvTZ1+3fYOEuehvOtXfTD3KENNUTo=";
	    
	    // Decode the Base64-encoded string to byte array
	    static byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
	    
	    // Create SecretKey from decoded bytes using SecretKeySpec
	   private final static   SecretKey SECRET_KEY = new SecretKeySpec(decodedBytes, SignatureAlgorithm.HS256.getJcaName()); // Replace "AES" with your desired algorithm
	    
    public static void main(String[] args) {
        // Generate a JWT token
        String jwtToken = generateJWTToken("divya");
        System.out.println("Generated JWT Token: " + jwtToken);
        System.out.println("Secret Key: " + Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded()));

        // Validate the JWT token
        Claims claims = validateJWTToken(jwtToken);
        if (claims != null) {
            System.out.println("JWT Token is valid.");
           
            System.out.println("Username: " + claims.get("username"));
        } else {
            System.out.println("JWT Token is invalid or expired.");
        }
    }

    private static SecretKey generateKey() {
        byte[] apiKeySecretBytes;
        try {
            // Generate a random 256-bit (32 bytes) key
            SecureRandom random = new SecureRandom();
            apiKeySecretBytes = new byte[32];
            random.nextBytes(apiKeySecretBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Convert the byte array to Base64-encoded string
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public static String generateJWTToken(String token) {
        Map<String, Object> claims = new HashMap<>();
 //       claims.put("password", "123");
        claims.put("username", token);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SECRET_KEY)
                .compact();
    }

 public static Claims validateJWTToken(String jwtToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwtToken).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException  | IllegalArgumentException e) {
            // Token validation failed
            return null;
        }
    }
}
