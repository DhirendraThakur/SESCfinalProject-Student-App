package com.example.studentapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET = "change-this-secret-key-to-a-long-random-value";
    private static final long EXPIRY_SECONDS = 60 * 60 * 24;

    private final ObjectMapper mapper = new ObjectMapper();

    public String generateToken(String id, String email, String role) {
        try {
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload = new HashMap<>();
            payload.put("id", id);
            payload.put("email", email);
            payload.put("role", role);
            payload.put("exp", Instant.now().getEpochSecond() + EXPIRY_SECONDS);

            String headerEncoded = base64Url(mapper.writeValueAsBytes(header));
            String payloadEncoded = base64Url(mapper.writeValueAsBytes(payload));
            String signature = sign(headerEncoded + "." + payloadEncoded);

            return headerEncoded + "." + payloadEncoded + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Could not generate token", e);
        }
    }

    public Map<String, Object> validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid token");
            }

            String expectedSignature = sign(parts[0] + "." + parts[1]);
            if (!expectedSignature.equals(parts[2])) {
                throw new RuntimeException("Invalid token signature");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> payload = mapper.readValue(payloadJson, Map.class);

            long exp = Long.parseLong(payload.get("exp").toString());
            if (Instant.now().getEpochSecond() > exp) {
                throw new RuntimeException("Token expired");
            }

            return payload;
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    private String sign(String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(key);
        return base64Url(hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private String base64Url(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
}