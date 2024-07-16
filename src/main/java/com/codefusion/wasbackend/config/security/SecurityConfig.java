package com.codefusion.wasbackend.config.security;
import com.codefusion.wasbackend.Account.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    public SecurityConfig(JwtUtil jwtUtil, @Lazy AccountService accountService) {
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtFilterUtil jwtFilterUtil = new JwtFilterUtil(jwtUtil);
        disableUnnecessaryHttpSecurityFeatures(http);
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/account/login").permitAll()
                                .requestMatchers("/webhook/**").permitAll()
                                .requestMatchers("/telegram/**").permitAll()
                                .requestMatchers("/account/**").hasAnyRole("ADMIN", "BOSS", "MANAGER", "EMPLOYEE")
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilterUtil, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void disableUnnecessaryHttpSecurityFeatures(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
