package com.example.shop.controller;

import com.example.shop.model.EmailType;
import com.example.shop.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/report/out-of-stock")
    public ResponseEntity<String> reportOutOfStock() {
        try {
            emailService.sendHtmlMail(EmailType.OUT_OF_STOCK);
            return ResponseEntity.ok("Sent!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
