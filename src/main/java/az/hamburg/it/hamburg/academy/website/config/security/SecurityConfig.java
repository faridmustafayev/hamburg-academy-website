package az.hamburg.it.hamburg.academy.website.config.security;

import az.hamburg.it.hamburg.academy.website.filter.JwtAuthFilter;
import az.hamburg.it.hamburg.academy.website.service.concrete.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class SecurityConfig {
    CustomUserDetailsService userDetailsService;
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(configure -> configure.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(GET,
                                        "/v1/specializations/**",
                                        "/v1/instructors/**",
                                        "/v1/syllabuses/**",
                                        "/v1/contacts/**",
                                        "/v1/course_images/**",
                                        "/v1/graduates/**",
                                        "/v1/services/**",
                                        "/v1/reviews/**",
                                        "/v1/diplomas/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/v1/auth/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/v1/users/**"
                                ).permitAll()

                                .requestMatchers(POST,
                                        "/v1/course-requests/**",
                                        "/v1/project-requests/**"
                                ).permitAll()

                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}