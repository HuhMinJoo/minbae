package com.minbae.config;

import com.minbae.config.jwt.JwtAuthenticationFilter;
import com.minbae.config.jwt.JwtAuthorizationFilter;
import com.minbae.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;


//@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final CorsFilter corsFilter;
	private final UserRepository userRepository;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(corsFilter)
				.formLogin().disable()
				.httpBasic().disable()
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
				.authorizeRequests()
				.antMatchers("/user/**")
				.access("hasRole('ROLE_USER')")
				.antMatchers("/owner/**")
				.access("hasRole('ROLE_OWNER')")
				.antMatchers("/deliver/**")
				.access("hasRole('ROLE_DELIVER')")
				.anyRequest().permitAll();


	}
}





