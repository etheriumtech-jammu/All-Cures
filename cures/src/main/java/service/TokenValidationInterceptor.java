package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TokenValidationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        System.out.println("User-Agent"+ userAgent);
 // Check if the user agent indicates a mobile device or MyApp
if (userAgent != null && (userAgent.toLowerCase().contains("mobile") || userAgent.contains("AllCures"))) {
    // If it's a mobile device or MyApp, skip token validation
    return true;
}
        
        String token = request.getHeader("Authorization");
        String url= request.getServletPath().toString();
        String final_url="";
        String pattern = "\\d+$";
        String url_result = url.replaceAll(pattern, "");

        if (!url.equals(url_result)) {
            final_url += url_result;
        } else {
            final_url+= url;
        }
        logger.info("TokenValidationInterceptor: Request received with token: {}", token);
        try {
            if (!(token == null )) {
                int result=TokenValidator.isValidToken(token,final_url);
                if(result==0)
                {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token.");
                    return false;
                }
                else if(result==2)
                {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You have crossed the Maximum Attempts.");
                }
            }
            else
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token.");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error during token validation: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during token validation.");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // This method is called after the handler method is invoked (after the API call).
        // You can perform additional actions here if needed.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // This method is called after the response has been sent to the client.
        // You can perform additional actions here if needed.
    }
}

