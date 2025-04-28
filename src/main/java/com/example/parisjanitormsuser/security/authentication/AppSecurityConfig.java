package com.example.parisjanitormsuser.security.authentication;
import com.example.parisjanitormsuser.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class AppSecurityConfig {

    private final UserRepo userRepo;

    public AppSecurityConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            log.info("Attempt to load user by email: {}",username);
            return userRepo.findByPrivateInfoEmail(username)
                    .map(user -> {
                        log.info("user founded "+user.getProfileInfo().getUsername());
                        return user;
                    })
                    .orElseThrow(()->{
                        log.error("user not found: {}", username);
                        return new UsernameNotFoundException("User not found");
                    });
        };
    }

    // Interface that defines authentication logic into Spring
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    // Global Handler that delegates authentication to one or several authentication provider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
