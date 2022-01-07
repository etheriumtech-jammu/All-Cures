package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

//import org.apache.tomcat.util.descriptor.web.SessionConfig;

import model.Registration;
import util.Constant;
import util.CookieManager;
import util.Test;



/**
 * Servlet Filter implementation class LogginFilters
 * Job: The Job of the Servlet Filter is to ensure that the user is logged in before they access this resource
 * 		If the user is not logged in, the filter should send the user to the login page with the destination URL as originally specified
 */
public class LogginFilters implements Filter {

	/**
	 * Default constructor. 
	 * 
	 */
	private ServletContext context;
	public LogginFilters() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		Constant.log("#########################LOGIN FILTER INVOKED####################", 0);
		
		HttpServletRequest req =(HttpServletRequest ) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		
		res.setHeader("Access-Control-Allow-Credentials", "true");

        // No Origin header present means this is not a cross-domain request
        String origin = req.getHeader("Origin");
         if (origin == null) {
//            // Return standard response if OPTIONS request w/o Origin header
           if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
        	   res.setHeader("Allow", "DELETE, HEAD, GET, OPTIONS, POST, PUT");
        	   res.setStatus(200);
                return;
            }
        } else {
            // This is a cross-domain request, add headers allowing access
        	res.setHeader("Access-Control-Allow-Origin", origin);
        	res.setHeader("Access-Control-Allow-Methods", "DELETE, HEAD, GET, OPTIONS, POST, PUT");

            String headers = req.getHeader("Access-Control-Request-Headers");
            if (headers != null)
            	res.setHeader("Access-Control-Allow-Headers", headers);

            // Allow caching cross-domain permission
            res.setHeader("Access-Control-Max-Age", "\""+Constant.DefaultPermCookieDuration+"\"");
        }
        
        
//		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//		res.setHeader("Access-Control-Allow-Headers", "access-control-allow-credentials");	


         // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
         if (req.getMethod().equals("OPTIONS")) {
//        	 res.setHeader("Access-Control-Allow-Origin", "http://192.168.29.160");
        	 res.setHeader("Access-Control-Allow-Origin", origin);
             res.setStatus(HttpServletResponse.SC_ACCEPTED);
             return;
         }
//    	 res.setHeader("Access-Control-Allow-Origin", origin);

		res.setHeader("Access-Control-Max-Age", "\""+Constant.DefaultPermCookieDuration+"\"");
//		res.setHeader("Access-Control-Allow-Headers", "x-requested-with");	
     	
		
		
		HttpSession session= req.getSession(true);
		Registration user = null;
		
		if(session.getAttribute(Constant.USER) != null){
			Constant.log("#########USER IS IN SESSION########", 0);
			//user = (Registration) session.getAttribute(Constant.USER);
		}else{
			//We will first check for if the user has asked to be signed on using Remember Me, which means that the Permanent Cookie is present
			//on their machine and travelling with the request object
			Constant.log("#########USER IS NOTTTTTTTTTTTT IN SESSION########", 0);
			CookieManager cookieMgr = new CookieManager();
			Cookie cookies[] = req.getCookies();
			if(cookies != null){  
				Constant.log("#########COOKIES ARE PRESENT IN REQ OBJECT BUT USER OBJ IS NOT IN SESSION########", 0);
				for (Cookie cookie : cookies) {
				     if (Constant.DefaultPermCookieName.equals(cookie.getName())) {
				    	 Constant.log("#########FOUND PERM COOKIE; DROPPING USER IN SESSION########", 0);
				          //Found the Perm Cookie; Which means the user opted for RememberMe
				    	 //Construct the User Object by reading the perm cookie and set the user object in session
				    	 user = cookieMgr.getUserFromPermCookie(cookie.getValue());
						 session.setAttribute(Constant.USER, user);
						//Also drop all the Cookies (session cookies)
						 cookieMgr.dropSessionCookies(res, user);				    	 
				    	 break;
				     }else if(Constant.SESSCOOK.equals(cookie.getName())) {
					     //Found the Session Cookie but not the perm cookie; Which means the user did not opt for Remember Me or 
				    	 //we have not encountered the Perm Cookie just yet
				    	 //Ideally this should never happen because if the user object is not in session, the sesscookie cannot be in session
				    	 //TODO: This Block should ideally be removed unless you wanna do anythihng at the filter level for every request to do with
				    	 //the session cookie e.g. changing a timestamp on the session cookie for every request that is made etc.
				     }
				}
			}
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		this.context = fConfig.getServletContext();
		this.context.log("LoginFilter initialized");
	}

}
