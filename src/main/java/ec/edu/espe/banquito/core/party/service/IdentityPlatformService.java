package ec.edu.espe.banquito.core.party.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class IdentityPlatformService {

    private static final String SIGN_UP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp";

    private final RestClient restClient;
    private final String apiKey;
    private final String defaultPassword;

    public IdentityPlatformService(@Value("${app.identity-platform.api-key}") String apiKey,
                                   @Value("${app.identity-platform.default-password}") String defaultPassword) {
        this.apiKey = apiKey;
        this.defaultPassword = defaultPassword;
        this.restClient = RestClient.create();
    }

    public void createAccount(String identification, String displayName) {
        String email = identification + "@banquito.internal";
        String safeDisplayName = (displayName == null || displayName.isBlank()) ? identification : displayName;

        try {
            this.restClient.post()
                    .uri(SIGN_UP_URL + "?key=" + this.apiKey)
                    .body(Map.of(
                            "email", email,
                            "password", this.defaultPassword,
                            "displayName", safeDisplayName,
                            "returnSecureToken", false
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            if (!e.getResponseBodyAsString().contains("EMAIL_EXISTS")) {
                throw new IllegalStateException(
                        "No se pudo crear la cuenta de acceso para " + identification, e);
            }
        }
    }
}
