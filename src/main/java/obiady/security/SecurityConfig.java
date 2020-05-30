package obiady.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration @EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	//dostarczenie żródła danych
	//@Autowired
//	DataSource dataSource;
	@Autowired
	private UserDetailsService userDetailsService; 

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/dinner").hasRole("USER")  //hasRole("ROLE_USER") will throw exception
			.antMatchers(
					"/h2-console/**",
					"/register",
					"/css/**").permitAll()
			//dodać zezwolenie do static resources (obrazy itp)
			.anyRequest().authenticated()
			.and()
		.logout()
			.permitAll()
			.logoutSuccessUrl("/login")
			.and()
		.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/dinner", true);
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
	
}
