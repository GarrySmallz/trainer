package de.trainer.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-Api-Key";

    private final ApiKeyProperties apiKeyProperties;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);

        if  (apiKey == null || apiKey.isBlank()) {
            throw new BadCredentialsException("Invalid API Key");
        }
        String expectedApiKey = apiKeyProperties.getKey();
        if (!MessageDigest.isEqual(expectedApiKey.getBytes(), apiKey.getBytes())) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);

    }
}
