package com.rbank.customer.statement.validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.rbank.customer.statement.validation.config.RestTemplateAutoConfiguration;


/**
 * @author Michael Philomin Raj
 *
 */

@SpringBootApplication
@Import({RestTemplateAutoConfiguration.class })
public class CustomerStatementValidatorOldApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerStatementValidatorOldApplication.class, args);
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(-1); // The default value of the property is 2097152 (2 MB)
		return multipartResolver;
	}

}
