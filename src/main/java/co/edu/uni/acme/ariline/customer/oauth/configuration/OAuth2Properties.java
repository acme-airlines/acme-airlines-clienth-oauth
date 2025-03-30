package co.edu.uni.acme.ariline.customer.oauth.configuration;

import co.edu.uni.acme.ariline.customer.oauth.dto.ClientDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the OAuth2 client.
 * <p>
 * Binds properties with the prefix {@code spring.security.oauth2}.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2")
@Data
public class OAuth2Properties {

    /**
     * The client details configuration.
     */
    private ClientDto client = new ClientDto();

    /**
     * The token URI for OAuth2.
     */
    private String tokenUri;
}
