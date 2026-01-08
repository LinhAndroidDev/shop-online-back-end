package com.example.shop.utils;

import com.google.auth.oauth2.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GmailOAuth2Authenticator {

    @Value("${gmail.oauth2.client-id}")
    private String clientId;

    @Value("${gmail.oauth2.client-secret}")
    private String clientSecret;

    @Value("${gmail.oauth2.access-token}")
    private String accessToken;

    @Value("${gmail.oauth2.refresh-token}")
    private String refreshToken;

    public String getAccessToken() throws IOException {
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();

        // Refresh token nếu cần (tự động refresh nếu expired)
        credentials.refreshIfExpired();

        com.google.auth.oauth2.AccessToken token = credentials.getAccessToken();
        if (token == null || token.getTokenValue() == null || token.getTokenValue().isEmpty()) {
            throw new IOException("Failed to obtain valid access token from OAuth2 credentials");
        }

        return token.getTokenValue();
    }
}
