package com.technical.test.prices.infrastructure.rest;

import com.technical.test.prices.application.security.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PriceControllerIntegrationTest {

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
    void givenCase1Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.price.amount").value(35.50))
                .andExpect(jsonPath("$.price.currency").value("EUR"));
    }

    @Test
    void givenCase2Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"))
                .andExpect(jsonPath("$.price.amount").value(25.45))
                .andExpect(jsonPath("$.price.currency").value("EUR"));
    }

    @Test
    void givenCase3Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.price.amount").value(35.50))
                .andExpect(jsonPath("$.price.currency").value("EUR"));
    }

    @Test
    void givenCase4Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.startDate").value("2020-06-15T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-15T11:00:00"))
                .andExpect(jsonPath("$.price.amount").value(30.50))
                .andExpect(jsonPath("$.price.currency").value("EUR"));
    }

    @Test
    void givenCase5Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.startDate").value("2020-06-15T16:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.price.amount").value(38.95))
                .andExpect(jsonPath("$.price.currency").value("EUR"));
    }

    @Test
    void givenMissingParameter_whenGetApplicablePrices_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION-002"))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    void givenInvalidParameterType_whenGetApplicablePrices_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION-001"))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    void givenNonExistentPrice_whenGetApplicablePrices_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/prices")
                        .with(tokenWithRole(Roles.USER))
                        .param("date", "2000-01-01T00:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("PRICE-001"))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    void givenNoToken_whenGetApplicablePrices_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(validPriceRequest())
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, startsWith("Bearer")))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("AUTH-002"))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    void givenTokenWithoutUserRole_whenGetApplicablePrices_thenReturnsForbidden() throws Exception {
        mockMvc.perform(validPriceRequest().with(tokenWithRole("OTHER")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.code").value("AUTH-003"))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    void givenAdminToken_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(validPriceRequest().with(tokenWithRole(Roles.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1));
    }

    private static MockHttpServletRequestBuilder validPriceRequest() {
        return get("/prices")
                .param("date", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1")
                .accept(MediaType.APPLICATION_JSON);
    }

    /**
     * Simulates an already validated token with the given role, without going through the login.
     */
    private static RequestPostProcessor tokenWithRole(String role) {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
