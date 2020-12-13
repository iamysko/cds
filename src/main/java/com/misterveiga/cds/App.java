/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * The Class App.
 */
@SpringBootApplication
public class App {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		// SpringApplication.run(App.class, args);
		final SpringApplicationBuilder builder = new SpringApplicationBuilder(App.class);
		builder.headless(false);
		final ConfigurableApplicationContext context = builder.run(args);
	}

}