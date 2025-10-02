package com.newsroom.config.exceptions;

import com.newsroom.dto.ResponseDTO.BaseOutput;
import com.newsroom.enums.ResponseStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.rmi.ServerException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("ERROR Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        BaseOutput.builder()
                                .errors(List.of(e.getMessage()))
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }

    @ExceptionHandler(value = ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException e) {
        log.error("ERROR ServerException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        BaseOutput.builder()
                                .errors(List.of(e.getMessage()))
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }

    @ExceptionHandler(value = JwtAuthenticationException.class)
    public ResponseEntity<Object> handleJwtAuthenticationException(JwtAuthenticationException e) {
        log.error("ERROR JwtAuthenticationException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        BaseOutput.builder()
                                .errors(List.of(e.getMessage()))
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        BaseOutput.builder()
                                .errors(
                                        ex.getBindingResult().getAllErrors().stream().map(
                                                        ObjectError::getDefaultMessage)
                                                .toList()
                                )
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("ERROR BadCredentialsException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        BaseOutput.builder()
                                .errors(List.of(ex.getMessage()))
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }

    @ExceptionHandler(value = NewsCommonException.class)
    public ResponseEntity<Object> handleTCCommonException(NewsCommonException e) {
        log.error("ERROR TCCommonException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        BaseOutput.builder()
                                .errors(List.of(e.getMessage()))
                                .statusCode(ResponseStatus.FAILED)
                                .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error("ERROR NoResourceFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        BaseOutput.builder()
                                .errors(List.of(ex.getMessage()))
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }

    /**
     * Handle ConstraintViolationException on controller layer
     *
     * @param ex: exception caught
     * @return ResponseEntity: with error
     */

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        BaseOutput.builder()
                                .errors(
                                        ex.getConstraintViolations().stream()
                                                .map(ConstraintViolation::getMessage)
                                                .toList()
                                )
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }

    @ExceptionHandler(value = StorageException.class)
    public ResponseEntity<BaseOutput<Object>> handleFileUploadException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        BaseOutput.builder()
                                .errors(List.of(ex.getMessage()))
                                .statusCode(ResponseStatus.FAILED)
                                .build()
                );
    }
}
