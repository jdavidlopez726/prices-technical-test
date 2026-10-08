package com.technical.test.prices.infrastructure.security.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private static final String USERNAME = "user";
    private static final String TOKEN_VALUE = "token-value";
    private static final Duration EXPIRATION = Duration.ofHours(1);

    @Mock
    private JwtEncoder jwtEncoder;

    private TokenService tokenService;

    @BeforeEach
    void setup() {
        tokenService = new TokenService(jwtEncoder, new JwtProperties(EXPIRATION));
    }

    @Test
    void givenAuthenticatedUser_whenIssue_thenSignsTokenWithSubjectRolesAndExpiry() {
        when(jwtEncoder.encode(any())).thenReturn(jwt(null));

        IssuedToken result = tokenService.issue(authentication("ROLE_USER", "ROLE_ADMIN"));

        assertThat(result).isEqualTo(new IssuedToken(TOKEN_VALUE, EXPIRATION));
        JwtClaimsSet claims = encodedClaims();
        assertThat(claims.getSubject()).isEqualTo(USERNAME);
        assertThat(claims.getClaimAsStringList("roles")).containsExactly("USER", "ADMIN");
        assertThat(Duration.between(claims.getIssuedAt(), claims.getExpiresAt())).isEqualTo(EXPIRATION);
    }

    @Test
    void givenAuthorityWithoutRolePrefix_whenIssue_thenLeavesItOutOfTheRolesClaim() {
        when(jwtEncoder.encode(any())).thenReturn(jwt(null));

        tokenService.issue(authentication("ROLE_USER", "SCOPE_read"));

        assertThat(encodedClaims().getClaimAsStringList("roles")).containsExactly("USER");
    }

    @Test
    void givenTokenWithRoles_whenToAuthentication_thenRestoresRolePrefix() {
        AbstractAuthenticationToken result = tokenService.toAuthentication(jwt(List.of("USER", "ADMIN")));

        assertThat(result.getName()).isEqualTo(USERNAME);
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void givenTokenWithoutRolesClaim_whenToAuthentication_thenHasNoAuthorities() {
        AbstractAuthenticationToken result = tokenService.toAuthentication(jwt(null));

        assertThat(result.getName()).isEqualTo(USERNAME);
        assertThat(result.getAuthorities()).isEmpty();
    }

    private JwtClaimsSet encodedClaims() {
        ArgumentCaptor<JwtEncoderParameters> parameters = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(parameters.capture());
        return parameters.getValue().getClaims();
    }

    private static Authentication authentication(String... authorities) {
        List<GrantedAuthority> grantedAuthorities = List.of(authorities).stream()
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
        return UsernamePasswordAuthenticationToken.authenticated(USERNAME, null, grantedAuthorities);
    }

    private static Jwt jwt(List<String> roles) {
        Jwt.Builder builder = Jwt.withTokenValue(TOKEN_VALUE)
                .header("alg", "RS256")
                .subject(USERNAME);
        if (roles != null) {
            builder.claim("roles", roles);
        }
        return builder.build();
    }
}
