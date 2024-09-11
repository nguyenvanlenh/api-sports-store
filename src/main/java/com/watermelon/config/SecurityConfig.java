package com.watermelon.config;

import static com.watermelon.utils.Constants.EndPoint.PUBLIC_ENDPOINTS;
import static com.watermelon.utils.Constants.EndPoint.SWAGGER_ENDPOINTS;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;

import com.watermelon.model.enumeration.ERole;
import com.watermelon.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	

	private static final String PRODUCTS_ENDPOINT = "/api/products/**";
	private static final String ORDERS_ENDPOINT = "/api/orders/**";
	private static final String RATINGS_ENDPOINT = "/api/ratings/**";
	private static final String PAYMENTS_ENDPOINT = "/api/payments/**";
	

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
				.cors(cors -> cors.configurationSource(request -> {
		            CorsConfiguration configuration = new CorsConfiguration();
		            configuration.setAllowedOrigins(List.of("http://localhost:3000"));
		            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		            configuration.setAllowedHeaders(List.of("*"));
		            return configuration;
		        }))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(authorize -> authorize
					    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
					    
					    .requestMatchers(HttpMethod.GET, PRODUCTS_ENDPOINT).permitAll()
					    .requestMatchers(HttpMethod.PUT, PRODUCTS_ENDPOINT).hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.PATCH, PRODUCTS_ENDPOINT).hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.DELETE, PRODUCTS_ENDPOINT).hasRole(ERole.ADMIN.toString())
					    
					    .requestMatchers(HttpMethod.GET, ORDERS_ENDPOINT).hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.POST, ORDERS_ENDPOINT).hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.PATCH, ORDERS_ENDPOINT).hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.DELETE, ORDERS_ENDPOINT).hasRole(ERole.ADMIN.toString())
					    
					    .requestMatchers(HttpMethod.GET, RATINGS_ENDPOINT).permitAll()
					    .requestMatchers(HttpMethod.POST, RATINGS_ENDPOINT).hasRole(ERole.USER.toString())
					    .requestMatchers(HttpMethod.DELETE, RATINGS_ENDPOINT).hasRole(ERole.USER.toString())
					    
					    
					    .requestMatchers(PAYMENTS_ENDPOINT).hasAnyRole(ERole.ADMIN.toString(),ERole.USER.toString())
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
