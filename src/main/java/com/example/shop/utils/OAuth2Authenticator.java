package com.example.shop.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

public class OAuth2Authenticator extends Authenticator {
    private final String userEmail;
    private final String accessToken;

    public OAuth2Authenticator(String userEmail, String accessToken) {
        this.userEmail = userEmail;
        this.accessToken = accessToken;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        // PasswordAuthentication(username, accessToken)
        return new PasswordAuthentication(userEmail, accessToken);
    }
}

