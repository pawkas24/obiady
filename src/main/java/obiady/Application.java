package obiady;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication  // zastępuje @Configuration @EnableAutoConfiguration @ComponentScan

public class Application {
	@Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
	@Bean
	public LayoutDialect layoutDialect() {
	    return new LayoutDialect();
	}
	
    @PostConstruct
    void started() {
      TimeZone.setDefault(TimeZone.getTimeZone("TimeZone"));
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);


	}

}	