package com.springsecurity.controller;

import com.springsecurity.model.JwtTokenDto;
import com.springsecurity.model.LoginRequestDto;
import com.springsecurity.model.RegistrationRequestDto;
import com.springsecurity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@Valid @RequestBody RegistrationRequestDto request) {
        authService.registration(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenDto> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.refreshToken(token));
    }
}
