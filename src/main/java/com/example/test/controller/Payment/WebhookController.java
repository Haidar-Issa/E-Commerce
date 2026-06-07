package com.example.test.controller.Payment;


import com.example.test.service.Payment.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestHeader("Stripe-Signature") String signatureHeader,
            HttpServletRequest request
    ) throws IOException {
        webhookService.webhook(signatureHeader,request);
        return ResponseEntity.ok().build();
    }
}
