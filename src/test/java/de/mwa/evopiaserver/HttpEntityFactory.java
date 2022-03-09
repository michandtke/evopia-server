package de.mwa.evopiaserver;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;

public class HttpEntityFactory {

    public static <T> HttpEntity<T> forTestUser() {
        return forUser("test@test.com", "test");
    }

    public static <T> HttpEntity<T> forUser(String username, String password) {
        return new HttpEntity<T>(createHeaders(username, password));
    }

    private static HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
