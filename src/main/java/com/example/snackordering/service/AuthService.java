package com.example.snackordering.service;

import com.example.snackordering.entity.AccountEntity;
import com.example.snackordering.entity.AuthEntity;
import com.example.snackordering.enums.AccountRole;
import com.example.snackordering.enums.TokenType;
import com.example.snackordering.model.RequestModel.AuthenticationRequest;
import com.example.snackordering.model.RequestModel.RegisterRequest;
import com.example.snackordering.model.RequestModel.SmsOTPRequest;
import com.example.snackordering.model.ResponseModel.AuthenticationResponse;
import com.example.snackordering.model.ResponseModel.RegisterResponse;
import com.example.snackordering.model.ResponseModel.SmsOTPResponse;
import com.example.snackordering.repository.AccountRepository;
import com.example.snackordering.repository.AuthRepository;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements LogoutHandler {

    @Value("${spring.application.security.sms-otp.twilio-account-sid}")
    private String accountSid;
    @Value("${spring.application.security.sms-otp.twilio-auth-token}")
    private String authToken;
    @Value("${spring.application.security.sms-otp.twilio-service-sid}")
    private String serviceSid;

    private final AuthenticationManager authenticationManager;
    private final AuthRepository authRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
        AuthEntity authEntity = authRepository.findByPhone(request.getPhone()).orElseThrow();

        Map<String, Object> extraClaims = new HashMap<>();
        String jwtToken = jwtService.generateAccessToken(extraClaims, authEntity.getPhone(), String.valueOf(authEntity.getRole()));
        String refreshToken = jwtService.generateRefreshToken(authEntity.getPhone());
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
                .phone(request.getPhone())
                .build();

        AuthEntity auth = AuthEntity.builder()
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AccountRole.USER)
                .isEnable(false)
                .isBlocked(false)
                .account(account)
                .build();

        account.setAuth(auth);

        AccountEntity savedAccount = accountRepository.save(account);
        authRepository.save(auth);

        return RegisterResponse.builder()
                .id(savedAccount.getAccountId())
                .phone(savedAccount.getPhone())
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        final String authenticationHeader = request.getHeader("Authorization");
        final String refreshToken;

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token is missing");
        }
        refreshToken = authenticationHeader.replace("Bearer ", "");
        final AuthEntity authEntity = authRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (jwtService.isTokenValid(refreshToken, authEntity, TokenType.REFRESH)) {
            Map<String, Object> extraClaims = new HashMap<>();
            String newAccessToken = jwtService.generateAccessToken(extraClaims, authEntity.getPhone(), String.valueOf(authEntity.getRole()));
            System.out.println("before return");
            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new RuntimeException("Refresh token is invalid");
    }

    public SmsOTPResponse generateSmsOtp(SmsOTPRequest smsOTPRequest) {
        Twilio.init(accountSid, authToken);

        Verification verification = Verification.creator(
                        serviceSid,
                        smsOTPRequest.getPhone(),
                        "sms")
                .create();
        return SmsOTPResponse.builder().phone(smsOTPRequest.getPhone()).status(verification.getStatus()).build();
    }

    public SmsOTPResponse verifyUserOTP(SmsOTPRequest smsOTPRequest) {
        Twilio.init(accountSid, authToken);
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                    .setTo(smsOTPRequest.getPhone())
                    .setCode(smsOTPRequest.getOtp())
                    .create();

            if ("approved".equalsIgnoreCase(verificationCheck.getStatus())) {
                AuthEntity authEntity = authRepository.findByPhone(smsOTPRequest.getPhone())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
                authEntity.setIsEnable(true);
                authRepository.save(authEntity);
            }

            return SmsOTPResponse.builder()
                    .phone(smsOTPRequest.getPhone())
                    .status(verificationCheck.getStatus())
                    .build();
        } catch (ApiException e) {
            if (e.getMessage().contains("Max check attempts reached")) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Maximum verification attempts reached. Please request a new OTP.");
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error verifying OTP: " + e.getMessage());
        }
    }



    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }
}
