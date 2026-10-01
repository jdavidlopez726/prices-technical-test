package com.technical.test.prices.infrastructure.security.rest.api;

import com.technical.test.prices.infrastructure.security.rest.dto.LoginRequest;
import com.technical.test.prices.infrastructure.security.rest.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "Access token issuing")
@RequestMapping("/auth")
public interface AuthApi {

    @Operation(summary = "Log in", description = "Validates the user credentials and returns a signed JWT access token, "
            + "to be sent on every request as 'Authorization: Bearer <token>'")
    @ApiResponse(responseCode = "200", description = "Authenticated",
            content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "400", description = "Missing or malformed request body")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @SecurityRequirements // Public endpoint: no token required
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request);
}
