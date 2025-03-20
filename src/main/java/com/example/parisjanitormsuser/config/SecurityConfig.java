package com.example.parisjanitormsuser.config;

import com.example.parisjanitormsuser.security.config.CustomAccessDeniedHandler;
import com.example.parisjanitormsuser.security.config.Http401UnauthorizedEntryPoint;
import com.example.parisjanitormsuser.security.config.JwtAuthenticationFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private  AuthenticationProvider authenticationProvider;
    @Autowired
    private Http401UnauthorizedEntryPoint unauthorizedEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF in local
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) // To be activated in prod
                // Secure CORS Configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/api/password/**",
                                        "/api/role/**",

                                        "/swagger-ui/index.html",
                                        "/swagger-ui-custom.html",
                                        "/swagger-ui.html",         // URL de Swagger UI
                                        "/swagger-ui/**",           // Ressources Swagger (CSS, JS, etc.)
                                        "/v3/api-docs/**",          // Endpoints OpenAPI
                                        "/v3/api-docs.yaml",

                                        "/api/logout"// Document YAML OpenAPI (si nécessaire)
                                ).permitAll()
                                /*.anyRequest().authenticated()*/)
                .logout(logout->logout
                        .logoutUrl("/api/auth/logout") //Definit la route de la deconnexion
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()) //Repondre 200 OK
                        .invalidateHttpSession(true) // Detruit la session
                        .clearAuthentication(true) // Effacer auth
                        .addLogoutHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext(); // Supprime la connexion en memoire
                        })
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new Http401UnauthorizedEntryPoint()))
                // 6️⃣ Deactivation de la session pour éviter les attaques CSRF via session hijacking
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider).addFilterBefore(
                        (Filter) jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Authorized HTTP methods
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Authorized headers
        corsConfiguration.setAllowCredentials(true);// Active les credentials pour permettre l'envoi de cookies ou autres informations d'authentification
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;

    }
}
