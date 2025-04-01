package co.edu.uni.acme.ariline.customer.oauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration for the OAuth2 client.
 * <p>
 * This configuration sets up CSRF, CORS, OAuth2 login, and endpoint authorization.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     *
     * <p>
     * - Disables CSRF protection.
     * - Permits all OPTIONS requests.
     * - Permits POST requests to "/auth/login" and GET requests to "/auth/authorized".
     * - Requires authentication for all other requests.
     * - Configures OAuth2 login with a custom login page.
     * - Enables OAuth2 client support.
     * - Applies CORS configuration.
     * </p>
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Allow OPTIONS for all endpoints
                        .requestMatchers(HttpMethod.OPTIONS, "/api/v1/**").permitAll()
                        // Allow the login and authorized endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/public/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/public/auth/authorized").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(login -> login.loginPage("/oauth2/authorization/oauth-client"))
                .oauth2Client(Customizer.withDefaults())
                .cors(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Creates a RestTemplate bean.
     *
     * @return a new RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Configures the CORS settings.
     *
     * <p>
     * The configuration includes:
     * - Allowed origins: e.g., "http://localhost:4200" (adjust as needed)
     * - Allowed HTTP methods: GET, POST, PUT, DELETE, OPTIONS
     * - Allowed headers: all headers
     * - Allowing credentials (cookies/authentication)
     * </p>
     *
     * @return a CorsConfigurationSource with the defined CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // List of allowed origins (adjust according to your environment, e.g., the URL of your Angular application)
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        // Allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allowed headers
        configuration.setAllowedHeaders(List.of("*"));
        // Allow credentials (cookies/authentication)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply the configuration to all routes
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
