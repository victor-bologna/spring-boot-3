package com.example.springboot.controllers;

import com.example.springboot.config.SecurityConfig;
import com.example.springboot.domain.dtos.LoginResponseDTO;
import com.example.springboot.domain.services.TokenService;
import com.example.springboot.domain.dtos.AuthenticationDTO;
import com.example.springboot.domain.dtos.RegisterDTO;
import com.example.springboot.domain.exception.UserAuthenticationException;
import com.example.springboot.domain.models.User;
import com.example.springboot.domain.services.UserService;
import jakarta.validation.Valid;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO authenticationData) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticationData.login(),
                authenticationData.password());
        Authentication authenticated = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        LoginResponseDTO token = new LoginResponseDTO(tokenService.generateToken((User) authenticated.getPrincipal()));

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterDTO registrationData) {
        UserDetails userDetails = this.userService.findByLogin(registrationData.login());
        if(!Objects.isNull(userDetails)) {
            throw new UserAuthenticationException("User already registered.");
        }

        String encryptedPassword = this.securityConfig.passwordEncoder().encode(registrationData.password());
        User newUser = new User(null, registrationData.login(), encryptedPassword, registrationData.role());

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(newUser));
    }
}
