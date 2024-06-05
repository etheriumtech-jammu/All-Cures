package service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.spy.memcached.compat.log.Logger;
import net.spy.memcached.compat.log.LoggerFactory;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

	public WebMvcConfig() {
        logger.info("WebMvcConfig is being processed.");
        
    }
	
//	@Autowired 
//	TokenValidationInterceptor interceptor;
	/*
	 @Override
	    public void addInterceptors(InterceptorRegistry registry) {
		 System.out.println("Adding Interceptors");
    	 logger.info("Adding Interceptors");
	      interceptor = new SimpleInterceptor();
	        registry.addInterceptor(interceptor)
	                .addPathPatterns("/cures/city/**")
	                .order(Ordered.HIGHEST_PRECEDENCE);
	    }
	 

    public void addInterceptors(InterceptorRegistry registry) {
    	System.out.println("Adding Interceptors");
    	 logger.info("Adding Interceptors");
    //	 registry.addInterceptor(new TokenValidationInterceptor())
    	 registry.addInterceptor(interceptor)
         .addPathPatterns("/cures/city/**")
         .order(Ordered.HIGHEST_PRECEDENCE);
    }
   */ 
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		logger.info("WebMvcConfig is processed.");
		 // Forward all routes to `index.html`
        registry.addViewController("/{spring:\\w+}")
                .setViewName("forward:/success.jsp");
        registry.addViewController("/**/{spring:\\w+}")
                .setViewName("forward:/success.jsp");
        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css|\\.png|\\.jpg|\\.jpeg|\\.gif|\\.svg|\\.ico|\\.html)$}")
                .setViewName("forward:/success.jsp");
    }
	
	
}



