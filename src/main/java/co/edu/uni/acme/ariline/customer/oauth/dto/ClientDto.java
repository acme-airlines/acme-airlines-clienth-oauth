package co.edu.uni.acme.ariline.customer.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Data Transfer Object (DTO) for client details.
 * <p>
 * This class holds the client's identifier, secret, and scopes for OAuth2 authentication.
 * </p>
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientDto {

    /**
     * The client identifier.
     */
    private String id;

    /**
     * The client secret.
     */
    private String secret;

    /**
     * The scopes associated with the client.
     */
    private String scopes;
}
