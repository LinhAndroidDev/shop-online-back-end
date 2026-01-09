package com.example.shop.controller;

import com.example.shop.model.EmailType;
import com.example.shop.response.BaseResponse;
import com.example.shop.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController extends BaseController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/report/out-of-stock")
    public ResponseEntity<BaseResponse<Void>> reportOutOfStock() {
        try {
            emailService.sendHtmlMail(EmailType.OUT_OF_STOCK);
            return successResponse("Sent!");
        } catch (Exception e) {
            return errorResponse(e.getMessage());
        }
    }
}
