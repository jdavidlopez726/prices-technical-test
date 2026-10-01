package com.technical.test.prices.infrastructure.security.login;

import com.technical.test.prices.infrastructure.security.token.IssuedToken;
import com.technical.test.prices.infrastructure.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Login flow: verifies the credentials and issues an access token for the authenticated user.
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public IssuedToken login(String username, String password) {
        //Authenticates user and throws exception if credentials invalid
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(username, password));

        //Issue token for already authenticated user
        return tokenService.issue(authentication);
    }
}
