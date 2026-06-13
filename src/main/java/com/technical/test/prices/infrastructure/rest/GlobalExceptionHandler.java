package com.technical.test.prices.infrastructure.rest;

import com.technical.test.prices.domain.exception.NotFoundException;
import com.technical.test.prices.infrastructure.rest.constant.RestErrorConstants;
import com.technical.test.prices.infrastructure.rest.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

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
                        RestErrorConstants.MSG_MISSING_PARAMETER.formatted(ex.getParameterName(), ex.getParameterType()),
                        status.value(),
                        RestErrorConstants.CODE_MISSING_PARAMETER));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expectedType = Optional.ofNullable(ex.getRequiredType())
                .map(Class::getSimpleName)
                .orElse("unknown");
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        RestErrorConstants.MSG_TYPE_MISMATCH.formatted(ex.getName(), expectedType, ex.getValue()),
                        status.value(),
                        RestErrorConstants.CODE_TYPE_MISMATCH));
    }
}
