package com.vr.project.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.vr.project"))
				.build()
				.useDefaultResponseMessages(false) 
				.apiInfo(apiInfo());
	}

	@SuppressWarnings("rawtypes")
	private ApiInfo apiInfo() {

	    return new ApiInfo(
	    	      "MINI AUTORIZADOR", 
	    	      "This is the simple REST API for a Test Company", 
	    	      "1.0.0", 
	    	      "Terms of service", 
	    	      new Contact("Huelton Santos", "huelton.github.io/profile", "hueltonsantos@gmail.com"),
	    	      "License of API", 
	    	      "https://www.apache.org/license.html", 
	    	      new ArrayList<VendorExtension>());
	}

}
