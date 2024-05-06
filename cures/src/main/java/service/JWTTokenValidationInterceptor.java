package service;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWTTokenValidationInterceptor implements HandlerInterceptor {

   
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // Retrieve the token from the request header
        String jwtToken = request.getHeader("Authorization");

        // Check if the token exists and starts with "Bearer "
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            // Extract the token without "Bearer " prefix
            jwtToken = jwtToken.substring(7);

            // Validate the JWT token and extract claims
            Claims claims = JWT.validateJWTToken(jwtToken);

            if (claims != null) {
                // Check if the username and password are valid
                String username = (String) claims.get("username");
      //          String password = (String) claims.get("password");

                // Here you can perform your custom authentication logic,
                // for example, checking against a user database, etc.
                if (isValidUser(username)) {
                    // User is authenticated, proceed with the request
                    return true;
                }
            }
        }

        // Token is invalid, missing, or authentication failed, send unauthorized response
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or unauthorized access");
        return false;
    }


 
    private static Claims validateJWTToken(String jwtToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwtToken).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException  | IllegalArgumentException e) {
            // Token validation failed
            return null;
        }
    }
    
    


    // Example method for custom authentication logic
    private boolean isValidUser(String username) {
        // Perform your custom authentication logic here
        // For demonstration purposes, let's assume username and password are valid
        return username.equals("divya");
    }
    static String encodedString = "ti0dG0Jy9RCttNVauQ1bjo0oYXNxfgHjfpAm/mKZaak=";
    
    // Decode the Base64-encoded string to byte array
    static byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
    
    // Create SecretKey from decoded bytes using SecretKeySpec
   public final static   SecretKey SECRET_KEY = new SecretKeySpec(decodedBytes, SignatureAlgorithm.HS256.getJcaName()); // Replace "AES" with your desired algorithm
    
}
