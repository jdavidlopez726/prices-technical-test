package com.technical.test.prices.infrastructure.security.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.technical.test.prices.infrastructure.security.token.IssuedToken;
import com.technical.test.prices.infrastructure.security.token.TokenService;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "secret";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginService loginService;

    @Test
    void givenValidCredentials_whenLogin_thenIssuesTokenForAuthenticatedUser() {
        Authentication authenticated = UsernamePasswordAuthenticationToken.authenticated(USERNAME, null, List.of());
        IssuedToken issuedToken = new IssuedToken("token-value", Duration.ofHours(1));
        when(authenticationManager.authenticate(any())).thenReturn(authenticated);
        when(tokenService.issue(authenticated)).thenReturn(issuedToken);

        IssuedToken result = loginService.login(USERNAME, PASSWORD);

        assertThat(result).isEqualTo(issuedToken);
        ArgumentCaptor<Authentication> credentials = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(credentials.capture());
        assertThat(credentials.getValue().getName()).isEqualTo(USERNAME);
        assertThat(credentials.getValue().getCredentials()).isEqualTo(PASSWORD);
        assertThat(credentials.getValue().isAuthenticated()).isFalse();
    }

    @Test
    void givenInvalidCredentials_whenLogin_thenPropagatesExceptionAndIssuesNoToken() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> loginService.login(USERNAME, PASSWORD))
                .isInstanceOf(BadCredentialsException.class);
        verifyNoInteractions(tokenService);
    }
}
