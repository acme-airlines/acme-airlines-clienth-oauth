package co.edu.uni.acme.ariline.customer.oauth.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for token responses.
 * <p>
 * Contains the access token, token type, ID token, expiration time, and scope information.
 * </p>
 */
@Data
public class TokenResponseDto {

    /**
     * The access token value.
     */
    private String accessToken;

    /**
     * The type of the token (e.g., "Bearer").
     */
    private String tokenType;

    /**
     * The ID token value.
     */
    private String idToken;

    /**
     * The number of seconds until the token expires.
     */
    private Integer expiresIn;

    /**
     * The scope associated with the token.
     */
    private String scope;
}
