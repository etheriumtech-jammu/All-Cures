package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class BeansConfig {

	private final static Logger logger = LoggerFactory.getLogger(BeansConfig.class);

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		//freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates"); // defines the classpath location of the
		freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/templates/"); // defines the classpath location of the
																			// freemarker templates
		freeMarkerConfigurer.setDefaultEncoding("UTF-8"); // Default encoding of the template files
		return freeMarkerConfigurer;
	}

}
