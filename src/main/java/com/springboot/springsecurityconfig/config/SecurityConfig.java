package com.springboot.springsecurityconfig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService detailsService() {
		UserDetails normalUser= User.withUsername("Ankit")
				.password(passwordEncoder().encode("ankit"))
				.roles("normal")
				.build();
		
		UserDetails adminUser=User.withUsername("admin")
				.password(passwordEncoder().encode("admin"))
				.roles("admin").build();
		
		InMemoryUserDetailsManager inMemoryUserDetailsManager =new InMemoryUserDetailsManager(normalUser,adminUser);
		return inMemoryUserDetailsManager;
	}
	@Bean
	public SecurityFilterChain chain(HttpSecurity httpSecurity)throws Exception{
		httpSecurity.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/home/admin/**").hasRole("admin")
			.requestMatchers("/home/normal/**").hasRole("normal")
			.requestMatchers("/home/public").permitAll()
			.anyRequest().authenticated().and().formLogin();
		return httpSecurity.build(); 
	}
	
}
