package com.technical.test.prices.infrastructure.security.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AuthControllerIntegrationTest {

    private static final String LOGIN_PATH = "/auth/login";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid username or password";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void givenValidCredentials_whenLogin_thenReturnsBearerToken() throws Exception {
        login("user", "user")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void givenUserCredentials_whenLogin_thenTokenContainsUserAndRole() throws Exception {
        JWTClaimsSet claims = loginClaims("user", "user");

        assertThat(claims.getSubject()).isEqualTo("user");
        assertThat(claims.getStringListClaim("roles")).containsExactly("USER");
        assertThat(claims.getExpirationTime()).isAfter(claims.getIssueTime());
    }

    @Test
    void givenAdminCredentials_whenLogin_thenTokenContainsAdminRole() throws Exception {
        JWTClaimsSet claims = loginClaims("admin", "admin");

        assertThat(claims.getSubject()).isEqualTo("admin");
        assertThat(claims.getStringListClaim("roles")).containsExactly("ADMIN");
    }

    @Test
    void givenWrongPassword_whenLogin_thenReturnsUnauthorized() throws Exception {
        login("user", "wrong-password")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("AUTH-001"))
                .andExpect(jsonPath("$.detail").value(INVALID_CREDENTIALS_MESSAGE));
    }

    @Test
    void givenUnknownUser_whenLogin_thenReturnsSameErrorAsWrongPassword() throws Exception {
        login("unknown", "user")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("AUTH-001"))
                .andExpect(jsonPath("$.detail").value(INVALID_CREDENTIALS_MESSAGE));
    }

    @Test
    void givenMalformedBody_whenLogin_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION-003"));
    }

    /**
     * Decodes the token without verifying its signature: only the claims written by the login are checked here.
     */
    private JWTClaimsSet loginClaims(String username, String password) throws Exception {
        String response = login(username, password)
                .andReturn().getResponse().getContentAsString();
        return SignedJWT.parse(JsonPath.read(response, "$.accessToken")).getJWTClaimsSet();
    }

    private ResultActions login(String username, String password) throws Exception {
        return mockMvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "username": "%s", "password": "%s" }
                        """.formatted(username, password)));
    }
}
