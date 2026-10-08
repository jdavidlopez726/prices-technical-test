package com.technical.test.prices.infrastructure.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.technical.test.prices.domain.exception.DomainErrorDefinitionEnum;
import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.infrastructure.rest.dto.ErrorResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void givenNotFoundException_whenHandle_thenReturnsNotFound() {
        NotFoundException ex = new NotFoundException(DomainErrorDefinitionEnum.PRICE_NOT_FOUND,
                1L, 35455L, LocalDateTime.parse("2020-06-14T10:00:00"));

        assertError(handler.handleNotFoundException(ex), HttpStatus.NOT_FOUND, "PRICE-001",
                "No applicable price found for brandId=1, productId=35455, date=2020-06-14T10:00");
    }

    @Test
    void givenNoResourceFoundException_whenHandle_thenReturnsNotFoundWithPath() {
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "/unknown", "unknown");

        assertError(handler.handleResourceNotFound(ex), HttpStatus.NOT_FOUND, "HTTP-001",
                "No endpoint found for '/unknown'");
    }

    @Test
    void givenUnsupportedMethod_whenHandle_thenReturnsMethodNotAllowedWithAllowHeader() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST", List.of("GET"));

        ResponseEntity<ErrorResponse> response = handler.handleMethodNotAllowed(ex);

        assertError(response, HttpStatus.METHOD_NOT_ALLOWED, "HTTP-002",
                "Method 'POST' is not supported for this endpoint");
        assertThat(response.getHeaders().getAllow()).containsExactly(HttpMethod.GET);
    }

    @Test
    void givenUnsupportedMethodWithoutSupportedMethods_whenHandle_thenReturnsEmptyAllowHeader() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<ErrorResponse> response = handler.handleMethodNotAllowed(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getHeaders().getAllow()).isEmpty();
    }

    @Test
    void givenMissingParameter_whenHandle_thenReturnsBadRequest() {
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("brandId", "Long");

        assertError(handler.handleMissingParameter(ex), HttpStatus.BAD_REQUEST, "VALIDATION-002",
                "Required parameter 'brandId' of type 'Long' is missing");
    }

    @Test
    void givenTypeMismatch_whenHandle_thenReturnsBadRequestWithExpectedType() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("abc", Long.class, "brandId", null, null);

        assertError(handler.handleTypeMismatch(ex), HttpStatus.BAD_REQUEST, "VALIDATION-001",
                "Parameter 'brandId' must be of type 'Long', got 'abc'");
    }

    @Test
    void givenTypeMismatchWithoutRequiredType_whenHandle_thenReportsUnknownType() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("abc", null, "brandId", null, null);

        assertError(handler.handleTypeMismatch(ex), HttpStatus.BAD_REQUEST, "VALIDATION-001",
                "Parameter 'brandId' must be of type 'unknown', got 'abc'");
    }

    @Test
    void givenMalformedRequestBody_whenHandle_thenReturnsBadRequest() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("JSON parse error", mock(HttpInputMessage.class));

        assertError(handler.handleMalformedRequestBody(ex), HttpStatus.BAD_REQUEST, "VALIDATION-003",
                "Request body is missing or malformed");
    }

    @Test
    void givenBadCredentials_whenHandle_thenReturnsUnauthorized() {
        assertError(handler.handleInvalidCredentials(new BadCredentialsException("Bad credentials")),
                HttpStatus.UNAUTHORIZED, "AUTH-001", "Invalid username or password");
    }

    @Test
    void givenDisabledAccount_whenHandle_thenReturnsSameMessageAsBadCredentials() {
        assertError(handler.handleInvalidCredentials(new DisabledException("User is disabled")),
                HttpStatus.UNAUTHORIZED, "AUTH-001", "Invalid username or password");
    }

    @Test
    void givenMissingToken_whenHandle_thenReturnsUnauthorized() {
        assertError(handler.handleInvalidToken(new InsufficientAuthenticationException("Full authentication required")),
                HttpStatus.UNAUTHORIZED, "AUTH-002", "Access token is missing, invalid or expired");
    }

    @Test
    void givenInvalidToken_whenHandle_thenReturnsUnauthorized() {
        OAuth2AuthenticationException ex = new OAuth2AuthenticationException(new OAuth2Error("invalid_token"));

        assertError(handler.handleInvalidToken(ex),
                HttpStatus.UNAUTHORIZED, "AUTH-002", "Access token is missing, invalid or expired");
    }

    @Test
    void givenAccessDenied_whenHandle_thenReturnsForbidden() {
        assertError(handler.handleAccessDenied(new AccessDeniedException("Access Denied")),
                HttpStatus.FORBIDDEN, "AUTH-003", "You do not have permission to access this resource");
    }

    @Test
    void givenUnexpectedException_whenHandle_thenReturnsInternalErrorWithoutLeakingDetails() {
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(new IllegalStateException("db password"));

        assertError(response, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL-001", "An unexpected error occurred");
    }

    private static void assertError(ResponseEntity<ErrorResponse> response, HttpStatus status, String code,
                                    String detail) {
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getBody()).isEqualTo(new ErrorResponse(detail, status.value(), code));
    }
}
