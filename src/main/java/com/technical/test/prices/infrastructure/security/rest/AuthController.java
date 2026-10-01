package com.technical.test.prices.infrastructure.security.rest;

import com.technical.test.prices.infrastructure.security.login.LoginService;
import com.technical.test.prices.infrastructure.security.rest.api.AuthApi;
import com.technical.test.prices.infrastructure.security.rest.dto.LoginRequest;
import com.technical.test.prices.infrastructure.security.rest.dto.TokenResponse;
import com.technical.test.prices.infrastructure.security.token.IssuedToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    private final LoginService loginService;

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest request) {
        IssuedToken token = loginService.login(request.username(), request.password());

        return ResponseEntity.ok(new TokenResponse(
                token.value(), BEARER_TOKEN_TYPE, token.expiresIn().toSeconds()));
    }
}
