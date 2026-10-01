package com.technical.test.prices.infrastructure.security.token;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * The only class that knows the content of the JWT access tokens: it writes them on login and reads them back on
 * every authenticated request.
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String ROLES_CLAIM = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    /**
     * Issues a signed token for an already authenticated user.
     */
    public IssuedToken issue(Authentication authentication) {
        Instant issuedAt = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(jwtProperties.expiration()))
                .subject(authentication.getName())
                .claim(ROLES_CLAIM, roles(authentication))
                .build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new IssuedToken(tokenValue, jwtProperties.expiration());
    }

    /**
     * Turns a token whose signature and expiry have already been verified into the authenticated user,
     * restoring the {@code ROLE_} prefix that {@link #issue} removed.
     */
    public AbstractAuthenticationToken toAuthentication(Jwt jwt) {
        List<GrantedAuthority> authorities = roles(jwt).stream()
                .<GrantedAuthority>map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * A token without the claim is treated as a user with no roles.
     */
    private static List<String> roles(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList(ROLES_CLAIM);
        return roles != null ? roles : List.of();
    }

    /**
     * Keeps only the roles, without their {@code ROLE_} prefix.
     */
    private static List<String> roles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith(ROLE_PREFIX))
                .map(authority -> authority.substring(ROLE_PREFIX.length()))
                .toList();
    }
}
