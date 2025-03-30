package co.edu.uni.acme.ariline.customer.oauth.controller;

import co.edu.uni.acme.ariline.customer.oauth.configuration.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

/**
 * REST controller for handling authentication requests.
 * <p>
 * Provides endpoints for authorization and login using the OAuth2 password grant.
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final OAuth2Properties oAuth2Props;
    private final RestTemplate restTemplate;

    /**
     * Endpoint for handling the authorization callback.
     *
     * @param code the authorization code received from the OAuth2 provider
     * @return a map containing the authorization code
     */
    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam String code) {
        return Collections.singletonMap("authorizationCode", code);
    }

    /**
     * Endpoint for user login using the OAuth2 password grant.
     *
     * @param credentials a map containing the "username" and "password"
     * @return a ResponseEntity with the token response or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Prepare the request body for the 'password' grant
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        body.add("scope", oAuth2Props.getClient().getScopes());

        // Build the Authorization header for CLIENT_SECRET_BASIC
        String clientId = oAuth2Props.getClient().getId();
        String clientSecret = oAuth2Props.getClient().getSecret();
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    oAuth2Props.getTokenUri(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            log.error("Authentication error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication error");
        }
    }
}
