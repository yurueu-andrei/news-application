package ru.clevertec.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.handling.handler.ExceptionSecurityFilter;
import ru.clevertec.security.filter.JwtAuthenticationFilter;

/**
 * Spring security configuration class. Details:csrf disabled, cors commented, /auth/login and /auth/register
 * endpoints available for anonymous users, get are available for everybody, /auth/users endpoint for getting
 * username by JWT token is available for everybody, other requests must be authorized,
 * 2 filters for authentication and exception handling
 *
 * @author Yuryeu Andrei
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ExceptionSecurityFilter exceptionFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
//                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
//                        .configurationSource(request -> {
//                            CorsConfiguration cors = new CorsConfiguration();
//                            cors.setAllowedOrigins(List.of("https://frontURL.ru"));
//                            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//                            cors.setAllowedHeaders(List.of("*"));
//                            return cors;
//                        }))
                .exceptionHandling(handling ->
                        handling.authenticationEntryPoint(exceptionFilter::handleException))
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST,
                                        "/auth/login",
                                        "/auth/register").anonymous()
                                .requestMatchers(HttpMethod.GET).permitAll()
                                .requestMatchers("/auth/users").permitAll()
                                .anyRequest().authenticated())

                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilter, JwtAuthenticationFilter.class);
        return http.build();
    }
}
