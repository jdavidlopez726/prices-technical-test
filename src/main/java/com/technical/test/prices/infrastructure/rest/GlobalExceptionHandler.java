package com.technical.test.prices.infrastructure.rest;

import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.infrastructure.rest.constant.RestErrorDefinitionEnum;
import com.technical.test.prices.infrastructure.rest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        status.value(),
                        ex.getError().getCode()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorDefinitionEnum.MISSING_PARAMETER.format(ex.getParameterName(), ex.getParameterType()),
                        status.value(),
                        RestErrorDefinitionEnum.MISSING_PARAMETER.getCode()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expectedType = Optional.ofNullable(ex.getRequiredType())
                .map(Class::getSimpleName)
                .orElse("unknown");
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorDefinitionEnum.TYPE_MISMATCH.format(ex.getName(), expectedType, ex.getValue()),
                        status.value(),
                        RestErrorDefinitionEnum.TYPE_MISMATCH.getCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedRequestBody(HttpMessageNotReadableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorDefinitionEnum.MALFORMED_REQUEST_BODY.getMessageTemplate(),
                        status.value(),
                        RestErrorDefinitionEnum.MALFORMED_REQUEST_BODY.getCode()));
    }

    /**
     * Failed login. Unknown user, wrong password and disabled account all return the same message on purpose,
     * so the response does not reveal which usernames exist.
     */
    @ExceptionHandler({BadCredentialsException.class, AccountStatusException.class})
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        log.debug("Login failed: {}", ex.getMessage());
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorDefinitionEnum.INVALID_CREDENTIALS.getMessageTemplate(),
                        status.value(),
                        RestErrorDefinitionEnum.INVALID_CREDENTIALS.getCode()));
    }

    /**
     * Request to a protected endpoint without a token, or with one that is not valid or has expired.
     */
    @ExceptionHandler({InsufficientAuthenticationException.class, OAuth2AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleInvalidToken(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        log.debug("Token rejected: {}", ex.getMessage());
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorDefinitionEnum.INVALID_TOKEN.getMessageTemplate(),
                        status.value(),
                        RestErrorDefinitionEnum.INVALID_TOKEN.getCode()));
    }

    /**
     * The token is valid, but the user does not have the role the endpoint requires.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorDefinitionEnum.ACCESS_DENIED.getMessageTemplate(),
                        status.value(),
                        RestErrorDefinitionEnum.ACCESS_DENIED.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error handling request", ex);
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorDefinitionEnum.INTERNAL_ERROR.getMessageTemplate(),
                        status.value(),
                        RestErrorDefinitionEnum.INTERNAL_ERROR.getCode()));
    }
}
