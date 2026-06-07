package com.example.test.service;

import com.example.test.dto.AuthResponse;
import com.example.test.dto.LoginRequest;
import com.example.test.dto.RegisterRequest;
import com.example.test.entity.RefreshToken;
import com.example.test.entity.User;
import com.example.test.enums.Role;
import com.example.test.exception.UserAlreadyExistsException;
import com.example.test.repository.RefreshTokenRepository;
import com.example.test.repository.UserRepository;
import com.example.test.security.JWT.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void register(RegisterRequest registerRequest) {

        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(STR."User with email \{registerRequest.getEmail()} already exists");
        }

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found: " + loginRequest.getEmail()));

        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = createRefreshToken(user);


        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    private RefreshToken createRefreshToken(User user) {

        if (refreshTokenRepository.existsByUser(user)) {
            refreshTokenRepository.deleteByUser(user);
            refreshTokenRepository.flush();
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(new ObjectId().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60));
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public AuthResponse refreshToken(String refreshToken) {

        RefreshToken refreshTokenObject = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("refresh token not found: " + refreshToken));

        if (refreshTokenObject.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("refresh token expired");
        }

        String newAccessToken = jwtService.generateToken(refreshTokenObject.getUser().getEmail());

        return new AuthResponse(newAccessToken, refreshTokenObject.getToken());
    }

    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found: " + email));

        refreshTokenRepository.deleteByUser(user);
    }
}
