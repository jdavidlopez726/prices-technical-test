package com.technical.test.prices.infrastructure.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PriceControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void givenCase1Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("date", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455))
                .andExpect(jsonPath("$[0].priceList").value(1))
                .andExpect(jsonPath("$[0].startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$[0].endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$[0].price").value(35.50));
    }

    @Test
    void givenCase2Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("date", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455))
                .andExpect(jsonPath("$[0].priceList").value(2))
                .andExpect(jsonPath("$[0].startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$[0].endDate").value("2020-06-14T18:30:00"))
                .andExpect(jsonPath("$[0].price").value(25.45));
    }

    @Test
    void givenCase3Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("date", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455))
                .andExpect(jsonPath("$[0].priceList").value(1))
                .andExpect(jsonPath("$[0].startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$[0].endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$[0].price").value(35.50));
    }

    @Test
    void givenCase4Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("date", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455))
                .andExpect(jsonPath("$[0].priceList").value(3))
                .andExpect(jsonPath("$[0].startDate").value("2020-06-15T00:00:00"))
                .andExpect(jsonPath("$[0].endDate").value("2020-06-15T11:00:00"))
                .andExpect(jsonPath("$[0].price").value(30.50));
    }

    @Test
    void givenCase5Params_whenGetApplicablePrices_thenReturnsSuccess() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("date", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455))
                .andExpect(jsonPath("$[0].priceList").value(4))
                .andExpect(jsonPath("$[0].startDate").value("2020-06-15T16:00:00"))
                .andExpect(jsonPath("$[0].endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$[0].price").value(38.95));
    }

    @Test
    void givenMissingParameter_whenGetApplicablePrices_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/prices")
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
    void givenNonExistentPrice_whenGetApplicablePrices_thenReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("date", "2000-01-01T00:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
