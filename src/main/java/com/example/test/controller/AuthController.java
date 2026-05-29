package com.example.test.Controller;

import com.example.test.DTO.*;
import com.example.test.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final String path = "/api/auth";

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse token = authService.login(loginRequest);
        ApiResponse<AuthResponse> response = ApiResponse.create(
                HttpStatus.OK,
                "Login successfully",
                token,
                path + "/login"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.OK,
                "User Registered Successfully",
                null,
                path + "/register"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        authService.logout(email);
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.OK,
                "Logged out successfully",
                null,
                path + "/logout"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
