package com.example.test.Controller.OTP;

import com.example.test.DTO.ApiResponse;
import com.example.test.Service.Email.MailService;
import com.example.test.Service.Otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/otp")
public class OtpController {

    private final OtpService otpService;
    final String path = "/api/auth/otp";

    @PostMapping("/send-email")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestParam String email) {
        otpService.sendOtp(email);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.create(
                        HttpStatus.OK,
                        "OTP sent to email successfully ",
                        email,
                        STR."\{path}send-email"
                )
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(
           @RequestParam String email,
           @RequestParam String otp) {

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.create(
                        HttpStatus.OK,
                        "OTP verification result: ",
                        otpService.isValidOtp(email,otp),
                        STR."\{path}/verify-email"
                )
        );
    }
}
