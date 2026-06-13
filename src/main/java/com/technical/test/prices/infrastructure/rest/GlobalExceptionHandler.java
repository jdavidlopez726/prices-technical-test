package com.technical.test.prices.infrastructure.rest;

import com.technical.test.prices.infrastructure.rest.constant.RestErrorDefinitionEnum;
import com.technical.test.prices.infrastructure.rest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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
