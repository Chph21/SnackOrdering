package com.example.snackordering.controller;

import com.example.snackordering.model.RequestModel.AuthenticationRequest;
import com.example.snackordering.model.RequestModel.RegisterRequest;
import com.example.snackordering.model.RequestModel.SmsOTPRequest;
import com.example.snackordering.model.ResponseModel.AuthenticationResponse;
import com.example.snackordering.model.ResponseModel.RegisterResponse;
import com.example.snackordering.model.ResponseModel.SmsOTPResponse;
import com.example.snackordering.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        AuthenticationResponse refreshTokenResponse = authService.refreshToken(request);
        return ResponseEntity.ok(refreshTokenResponse);
    }

    @PostMapping(value = "/generate-sms-otp")
    public ResponseEntity<SmsOTPResponse> generateSmsOtp(@Valid @RequestBody SmsOTPRequest smsOTPRequest) {
        SmsOTPResponse response = authService.generateSmsOtp(smsOTPRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-sms-otp")
    public ResponseEntity<SmsOTPResponse> verifyUserOTP(@RequestBody SmsOTPRequest smsOTPRequest) {
        SmsOTPResponse response = authService.verifyUserOTP(smsOTPRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-authentication")
    public ResponseEntity<String> testAuthentication() {
        return ResponseEntity.ok("Authentication successful");
    }

}
