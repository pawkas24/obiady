package obiady;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
public class ObiadyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObiadyApplication.class, args);
	}
	
	//obsługa polskich znaków w linkach (request param)
	@Bean
	public FilterRegistrationBean registerEncodingFilter() {
	    CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
	    encodingFilter.setEncoding("UTF-8");
	    encodingFilter.setForceEncoding(true);
	    FilterRegistrationBean registration = new FilterRegistrationBean(encodingFilter);
	    registration.addUrlPatterns("/*");
	    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
	    return registration;
	}

}
