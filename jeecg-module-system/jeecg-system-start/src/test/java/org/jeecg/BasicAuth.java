package org.jeecg;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuth {

    public static void main(String[] args) {
        String username = "057e53c1ddb88c71";
        String password = "5b144d2678ede61535a8e96a1231d2eb";

        // Encode the username and password using Base64
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        // Set the Authorization header with the encoded credentials
        String authorizationHeader = "Basic " + encodedCredentials;
        System.out.println("Authorization header: " + authorizationHeader);
    }
}
