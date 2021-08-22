package org.inu.universe.exception.advice;

import org.inu.universe.exception.*;
import org.inu.universe.model.common.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandling(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(null, errors));
    }

    // 권한 오류
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ExceptionResponse> handlingException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(e.getMessage(), null));
    }

    // 인증 오류
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ExceptionResponse> handlingExceptionl(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(e.getMessage(), null));
    }

    @ExceptionHandler({EmailException.class, AccountException.class, ProfileException.class, HashTagException.class, IdealTypeException.class, IllegalStateException.class})
    public ResponseEntity InvalidReqExceptionHandling(RuntimeException e) {
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
