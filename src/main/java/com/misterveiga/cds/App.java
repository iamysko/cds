/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RestController;


/**
 * The Class App.
 */
@SpringBootApplication
@RestController
public class App {


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */


	public static void main(final String[] args) {
		SpringApplication.run(App.class, args);
	}

}

@SpringBootApplication
@RestController
class SocialApplication extends WebSecurityConfigurerAdapter {

	// ...

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeRequests(a -> a
						.antMatchers("/", "/error", "/webjars/**","/rdss/**").permitAll()
						.anyRequest().authenticated()
				)
				.exceptionHandling(e -> e
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				)
				.oauth2Login();

		// @formatter:on
	}
}



