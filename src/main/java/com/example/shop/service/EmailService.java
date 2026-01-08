package com.example.shop.service;

import com.example.shop.model.EmailType;
import com.example.shop.model.OutOfStock;
import com.example.shop.utils.Constants;
import com.example.shop.utils.GmailOAuth2Authenticator;
import com.example.shop.utils.OAuth2Authenticator;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private GmailOAuth2Authenticator auth;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private InventoryService inventoryService;

    public void sendHtmlMail(EmailType type) throws Exception {
        // Get access token
        String accessToken = auth.getAccessToken();

        JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;

        // Tạo context cho Thymeleaf
        String html;

        switch (type) {
            case OUT_OF_STOCK:
                html = getHtmlOutOfStock(inventoryService.getOutOfStockProducts(), templateEngine);
                break;
            default:
                throw new IllegalArgumentException("Unsupported email type: " + type);
        }

        // Tạo Properties object mới với đầy đủ cấu hình
        Properties props = new Properties();

        // Copy properties từ sender (nếu có)
        props.putAll(sender.getJavaMailProperties());

        // Đảm bảo host và port được set đúng
        String host = sender.getHost() != null ? sender.getHost() : "smtp.gmail.com";
        int port = sender.getPort() > 0 ? sender.getPort() : 587;

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        // Timeout settings
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        // Cấu hình OAuth2
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.smtp.sasl.enable", "true");
        props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
        props.put("mail.smtp.auth.login.disable", "true");
        props.put("mail.smtp.auth.plain.disable", "true");

        // Debug (có thể bật để xem log)
        // props.put("mail.debug", "true");

        // Tạo Session với Properties đã được cấu hình đúng
        Session session = Session.getInstance(
                props,
                new OAuth2Authenticator(Constants.GMAIL_SENDER, accessToken)
        );

        MimeMessage message = new MimeMessage(session);
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(Constants.GMAIL_SENDER);
        helper.setTo("linhnh@zyyx.jp");
        helper.setSubject(type.getValue());
        helper.setText(html, true);

        // Thực hiện gửi với error handling tốt hơn
        // Với OAuth2, sử dụng Transport.send() trực tiếp vì authentication đã được xử lý trong Session
        Transport.send(message);
    }

    @NotNull
    private static String getHtmlOutOfStock(List<OutOfStock> req, TemplateEngine templateEngine) {
        Context context = new Context();
        context.setVariable("reportDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("dashboardUrl", "http://localhost:5173/inventory");
        context.setVariable("items", req);
        // Render HTML từ template
        String html = templateEngine.process("out-of-stock-report.html", context);
        return html;
    }
}
