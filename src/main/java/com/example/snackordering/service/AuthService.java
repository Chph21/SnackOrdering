package com.example.snackordering.service;

import com.example.snackordering.entity.AccountEntity;
import com.example.snackordering.entity.AuthEntity;
import com.example.snackordering.enums.AccountRole;
import com.example.snackordering.enums.TokenType;
import com.example.snackordering.model.RequestModel.AuthenticationRequest;
import com.example.snackordering.model.RequestModel.RegisterRequest;
import com.example.snackordering.model.ResponseModel.AuthenticationResponse;
import com.example.snackordering.model.ResponseModel.RegisterResponse;
import com.example.snackordering.repository.AccountRepository;
import com.example.snackordering.repository.AuthRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements LogoutHandler {

    private final AuthenticationManager authenticationManager;
    private final AuthRepository authRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        AuthEntity authEntity = authRepository.findByEmail(request.getEmail()).orElseThrow();

        Map<String, Object> extraClaims = new HashMap<>();
        String jwtToken = jwtService.generateAccessToken(extraClaims, authEntity.getEmail(), String.valueOf(authEntity.getRole()));
        String refreshToken = jwtService.generateRefreshToken(authEntity.getEmail());
        // Update the refresh token in the database
        authEntity.setRefreshToken(refreshToken);
        authRepository.save(authEntity);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    public RegisterResponse register(RegisterRequest request) {
        AccountEntity account = AccountEntity.builder()
                .email(request.getEmail())
                .build();

        AuthEntity auth = AuthEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AccountRole.USER)
                .isEnable(true)
                .isBlocked(false)
                .account(account)
                .build();

        account.setAuth(auth);

        AccountEntity savedAccount = accountRepository.save(account);
        authRepository.save(auth);

        return RegisterResponse.builder()
                .id(savedAccount.getAccountId())
                .email(savedAccount.getEmail())
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authenticationHeader = request.getHeader("Authorization");
        final String refreshToken;
        // ? if authenticationHeader is null or does not start with "Bearer ", jump to
        // the next filter
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token is missing");
        }
        refreshToken = authenticationHeader.replace("Bearer ", "");
        // todo EXTRACT EMAIL FROM JWT TOKEN
        final AuthEntity authEntity = authRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (jwtService.isTokenValid(refreshToken, authEntity, TokenType.REFRESH)) {
            Map<String, Object> extraClaims = new HashMap<>();
            String newAccessToken = jwtService.generateAccessToken(extraClaims, authEntity.getEmail(), String.valueOf(authEntity.getRole()));
            System.out.println("before return");
            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new RuntimeException("Refresh token is invalid");
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }
}
