package fsa.cplorm.config;

import fsa.cplorm.security.JpaUserDetailsService;
import fsa.cplorm.security.jwt.JwtTokenProvider;
import fsa.cplorm.security.jwt.TokenProvider;
import fsa.cplorm.security.jwt.authentication.JwtAuthenticationConverter;
import fsa.cplorm.security.jwt.authentication.JwtAuthenticationFilter;
import fsa.cplorm.security.jwt.authentication.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JpaUserDetailsService jpaUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
//                        .requestMatchers("/api/student").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/api/student/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_STUDENT")
                        .requestMatchers("/api/student").hasRole("ADMIN")
                        .requestMatchers("/api/student/{id}").hasAnyRole("ADMIN", "STUDENT")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                .userDetailsService(jpaUserDetailsService)
                .addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenProvider tokenProvider(@Value("${security.jwt.secret:changeit}") String secret,
                                       @Value("${security.jwt.issuer:cpl-orm}") String issuer,
                                       @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
        return new JwtTokenProvider(secret, issuer, expirationSeconds);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(TokenProvider tokenProvider, UserDetailsService userDetailsService) {
        return new JwtAuthenticationProvider(tokenProvider, userDetailsService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtAuthenticationProvider jwtAuthenticationProvider) {
        AuthenticationManager authenticationManager = new ProviderManager(jwtAuthenticationProvider);
        return new JwtAuthenticationFilter(authenticationManager, new JwtAuthenticationConverter());
    }
}
