package genecar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import genecar.common.web.AuthCheckInterceptor;
import genecar.common.web.CommonInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	
	@Autowired
	@Qualifier("commonInterceptor")
	CommonInterceptor commonInterceptor;
	
	@Autowired
	@Qualifier("authCheckInterceptor")
	AuthCheckInterceptor authCheckInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonInterceptor)
				.addPathPatterns("/api/**")
				.addPathPatterns("/api/v2/**")
				.addPathPatterns("/api/v3/**")
				.order(1);
		registry.addInterceptor(authCheckInterceptor)
				.addPathPatterns("/api/v2/**")
				.addPathPatterns("/api/v3/**")
	            .excludePathPatterns( "/logout",  "/css/**").order(2);
	}

}