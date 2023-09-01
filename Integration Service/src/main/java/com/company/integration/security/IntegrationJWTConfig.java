package com.company.integration.security;

import com.company.core.repository.UserRepository;
import com.company.core.security.JWTFilterValidator;
import com.company.core.security.auth.UserDetailsSeviceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// @EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class IntegrationJWTConfig {

    private final UserDetailsSeviceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final Environment environment;

    public IntegrationJWTConfig(UserDetailsSeviceImpl userService, PasswordEncoder passwordEncoder, UserRepository userRepository, Environment environment) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.environment = environment;
    }

    @Bean
    AuthenticationManager reportAuthenticationManager(HttpSecurity http) throws Exception{ //Used to configure and create an instance of the AuthenticationManager in a Spring Security configuration.
        return http.getSharedObject(AuthenticationManagerBuilder.class) // Retrieve the AuthenticationManagerBuilder instance from the HttpSecurity object.
            .userDetailsService(userService) // Sets the UserDetailsSeviceImpl to be used by the AuthenticationManager
            .passwordEncoder(passwordEncoder).and().build(); // Builds the AuthenticationManager instance based on the configured settings.
    }

    @Bean
    SecurityFilterChain reportFilterChain(HttpSecurity http) throws Exception{
        
        http
            .csrf(csrf -> csrf.disable())//Remove this line when in production mode
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/api/v2/integration/common/**").permitAll()
                .anyRequest().authenticated()
            );
        
        http.addFilter(new JWTFilterValidator(reportAuthenticationManager(http), userRepository,environment));

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }

}
