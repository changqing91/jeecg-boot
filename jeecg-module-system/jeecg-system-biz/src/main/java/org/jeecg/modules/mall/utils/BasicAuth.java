package org.jeecg.modules.mall.utils;

import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuth {

    public static String generateAuth(String appKey, String appSecret) {
        // Encode the username and password using Base64
        String credentials = appKey + ":" + appSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        // Set the Authorization header with the encoded credentials
        String authorizationHeader = "Basic " + encodedCredentials;
        System.out.println("Authorization header: " + authorizationHeader);
        return authorizationHeader;
    }
}
