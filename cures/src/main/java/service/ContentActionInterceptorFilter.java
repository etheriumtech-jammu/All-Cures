package service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/content")
public class ContentActionInterceptorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code (if needed)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String userAgent = httpRequest.getHeader("User-Agent");
   	 System.out.println("User-Agent"+ userAgent);
   	if (userAgent != null && (userAgent.toLowerCase().contains("mobile") || userAgent.contains("AllCures"))) {
	    // If it's a mobile device or MyApp, skip token validation
	    return ;
	}
        String uri = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();

        System.out.println("Intercepted URL: " + uri);
        System.out.println("queryString in  URL: " + queryString);
        // Check if the request URL matches /SearchActionController and cmd parameter is set to getResults
 //       if ("/cures/content".equals(uri) && queryString != null && queryString.contains("cmd=getResults")) {
        if ("/cures/content".equals(uri) && queryString != null ) {
            // Your validation logic here
            // Example: Check if a specific token exists in the request header
        	System.out.println("hello");
            String token = httpRequest.getHeader("Authorization");
            System.out.println(TokenValidator.isValidToken(token, uri));
            try {
                if (!(token == null )) {
                    int result=TokenValidator.isValidToken(token,uri);
                    if(result==0)
                    {
                    	httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token.");
                        return;
                    }
                    else if(result==2)
                    {
                    	httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You have crossed the Maximum Attempts.");
                    }
                }
                else
                {
                	httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token.");
                    return ;
                }
            } catch (Exception e) {
                // Handle the exception
                e.printStackTrace(); // Print the stack trace for debugging
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during token validation.");
                return;
            }
        }
        
        // If the request does not match the specified criteria, continue processing without validation
  //      chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code (if needed)
    }
}