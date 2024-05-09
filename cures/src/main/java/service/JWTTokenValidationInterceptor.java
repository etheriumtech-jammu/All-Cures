package service;

import io.jsonwebtoken.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.SecretKey;
import org.apache.commons.io.IOUtils;
public class JWTTokenValidationInterceptor implements HandlerInterceptor {

    // Define constants
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String[] VALID_USERNAMES = {"amangill@etheriumtech.com", "ruler.here@gmail.com", "kauraman198495@gmail.com","dikshapandita313@gmail.com"};
    private static final List<String> VALID_USERNAMES_LIST = Arrays.asList(VALID_USERNAMES);
    private static final String SECRET_KEY_BASE64 = "ti0dG0Jy9RCttNVauQ1bjo0oYXNxfgHjfpAm/mKZaak=";
    
    // Decode the Base64-encoded string to byte array
    private static final byte[] SECRET_KEY_BYTES = java.util.Base64.getDecoder().decode(SECRET_KEY_BASE64);

    // Create SecretKey from decoded bytes using SecretKeySpec
    private static final SecretKey SECRET_KEY = new javax.crypto.spec.SecretKeySpec(SECRET_KEY_BYTES, SignatureAlgorithm.HS256.getJcaName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // Retrieve the token from the request header
       
        String jwtToken = request.getHeader(AUTH_HEADER);
        String url= request.getServletPath().toString();
        String str="/article/create/new";
       
        // Check if the token exists and starts with "Bearer "
        if (jwtToken != null && jwtToken.startsWith(BEARER_PREFIX)) {
            // Extract the token without "Bearer " prefix
            jwtToken = jwtToken.substring(BEARER_PREFIX.length());

            // Validate the JWT token and extract claims
            Claims claims = validateJWTToken(jwtToken);

            if (claims != null) {
                // Check if the username is valid
                String username = (String) claims.get("username");
               
               if (isValidUser(username) ) {
                    // User is authenticated, proceed with the request
                    return true;
                }
               else if(url.equals(str))
                {
                	String status = getStatusFromJson(request);
                	if(status=="2")
                	{
                		return true;
                	}
                }
            }
        }

        // Token is invalid, missing, or authentication failed, send unauthorized response
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or unauthorized access");
        return false;
    }

    private Claims validateJWTToken(String jwtToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jwtToken).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            // Token validation failed
            return null;
        }
    }

    //to generate token
     public static String generateJWTToken(String token) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", token);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
       //         .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SECRET_KEY)
                .compact();
    }
    // Extract status from request JSON, default to 1 if not present or not an integer
    private String getStatusFromJson(HttpServletRequest request) throws IOException {
        String requestJsonStr = IOUtils.toString(request.getInputStream(), "UTF-8");
        ObjectMapper mapper = new ObjectMapper();
       Map<String, Object> requestJsonMap = mapper.readValue(requestJsonStr, Map.class);
        Object articleStatusObj = requestJsonMap.get("articleStatus");
        if (articleStatusObj instanceof String) {
            return (String) articleStatusObj;
        }
        return ""; // Default status if not present or not an integer
    }

    // Check if the username is valid
    private boolean isValidUser(String username) {
        
        return VALID_USERNAMES_LIST.contains(username);
    }

   
    
}
