package com.example.test.Service.Otp;

import com.example.test.Service.Email.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final MailService mailService;
    private final RedisTemplate<String, String> redisTemplate;
    final int MAX_ATTEMPTS = 5;
    final long SEND_LIMIT_TTL = 600;
    final int MAX_VERIFY_ATTEMPTS = 5;
    final long VERIFY_LIMIT_TTL = 600;

    public void sendOtp(String email) {

        checkSendLimit(email);

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        long OTP_TTL = 640;
        redisTemplate.opsForValue().set(STR."OTP:\{email}",
                otp,
                OTP_TTL,
                TimeUnit.SECONDS);

        mailService.sendOtp(email, otp);
    }

    public boolean isValidOtp(String email, String otp) {

        checkVerifyLimit(email);

        String key = STR."OTP:\{email}";
        String sortedOtp = redisTemplate.opsForValue().get(key);

        if (sortedOtp == null) return false;

        boolean valid = sortedOtp.equals(otp);

        if (valid) {
            redisTemplate.delete(key);
        }
        return valid;

    }

    public void checkSendLimit(String email) {
        String Key = STR."OTP:SEND:\{email}";

        Long attempts = redisTemplate.opsForValue().increment(Key);

        if (attempts == 1) {
            redisTemplate.expire(Key, SEND_LIMIT_TTL, TimeUnit.SECONDS);
        }

        if (attempts > MAX_ATTEMPTS) {
            throw new RuntimeException("Too many OTP requests. Try again later.");
        }
    }

    public void checkVerifyLimit(String email) {
        String key = STR."OPT:VERIFY:\{email}";

        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts == 1) {
            redisTemplate.expire(key, VERIFY_LIMIT_TTL, TimeUnit.SECONDS);
        }

        if (attempts > MAX_VERIFY_ATTEMPTS) {
            throw new RuntimeException("Too many OTP requests. Try again later.");
        }
    }
}
