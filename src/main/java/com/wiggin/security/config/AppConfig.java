package com.wiggin.security.config;

import com.wiggin.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService service(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return repository.findByEmail(username)
                        .orElseThrow(()->new UsernameNotFoundException("Use name or Email is not found"));
            }
        };
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider provider(){
        var provide =  new DaoAuthenticationProvider();
        provide.setUserDetailsService(service());
        provide.setPasswordEncoder(encoder());
        return provide;
    }



    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {

       return configuration.getAuthenticationManager();
    }
}
