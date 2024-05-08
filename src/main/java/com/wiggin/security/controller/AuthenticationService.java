package com.wiggin.security.controller;
import com.wiggin.security.entity.Role;
import com.wiggin.security.entity.User;
import com.wiggin.security.repository.UserRepository;
import com.wiggin.security.service.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private final AuthenticationManager manager;
    public AuthenticationResponse register(RegisterRequest request) {

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        String token = service.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        String token = service.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
