package com.example.main.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;

@Service
public class GoogleAuthenticationService {

    Logger logger = LoggerFactory.getLogger(GoogleAuthenticationService.class);

    @Value("${clientID}")
    private String googleAuthenticationClientID;

    public HashMap<String, String> validateGoogleToken(String authentication) throws Exception {

        logger.info("Token Validation");

        HashMap<String, String> hashMap = new HashMap<>();

        if (StringUtils.isEmpty("authentication")) {
            logger.error("Token is missing in the request given");
            throw new Exception("Token is Missing!");
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleAuthenticationClientID)).build();

        GoogleIdToken idToken = verifier.verify(authentication);
        logger.info("Checking " + idToken);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            logger.info("response " + payload);
            hashMap.put("email", payload.getEmail());
            hashMap.put("name", (String) payload.get("name"));
        } else {

        }
        return hashMap;
    }
}
