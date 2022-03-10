package de.mwa.evopiaserver;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public class HttpEntityFactory {
    private static final String TEST_USERNAME_WITH_PROFILE = "Batman@waynecorp.com";
    private static final String TEST_USERNAME_WITHOUT_PROFILE = "Robin@waynecorp.com";
    private static final String TEST_PASSWORD = "test";

    public static <T> HttpEntity<T> forTestUser() {
        return forUser(TEST_USERNAME_WITH_PROFILE, TEST_PASSWORD);
    }

    public static <T> HttpEntity<T> forTestUserWithoutProfile() {
        return forUser(TEST_USERNAME_WITHOUT_PROFILE, TEST_PASSWORD);
    }

    public static <T> HttpEntity<T> forTestUserWith(T body) {
        return forUserWith(body, TEST_USERNAME_WITH_PROFILE, TEST_PASSWORD);
    }

    public static <T> HttpEntity<T> forUserWith(T body, String username, String password) {
        return new HttpEntity<T>(body, headersWithMediaType(username, password));
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

    private static HttpHeaders headersWithMediaType(String username, String password) {
        HttpHeaders headers = createHeaders(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
