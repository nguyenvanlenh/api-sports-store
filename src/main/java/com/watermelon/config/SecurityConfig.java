package com.watermelon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.watermelon.utils.Constants.EndPoint.*;

import com.watermelon.model.enumeration.ERole;
import com.watermelon.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	

	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(authorize -> authorize
					    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
					    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
					    .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.PATCH, "/api/products/**").hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.GET, "/api/orders/**").hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.POST, "/api/orders/**").hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.PATCH, "/api/orders/**").hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.POST, "/api/ratings/**").hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.DELETE, "/api/ratings/**").hasRole(ERole.USER.toString())
					    
					    .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole(ERole.ADMIN.toString())
					    .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole(ERole.ADMIN.toString())
					    
					    .anyRequest().authenticated()
					)

				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

				.httpBasic(Customizer.withDefaults())
				.exceptionHandling(handling ->
						handling.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
		;

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(SWAGGER_ENDPOINTS);
	}
}
