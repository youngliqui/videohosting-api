package ru.clevertec.videohosting_api.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.videohosting_api.exception.*;

import java.security.SignatureException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<HttpStatus> handleValidationException(CustomValidationException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameAlreadyExistsException.class, EmailAlreadyExistsException.class})
    public ResponseEntity<HttpStatus> handleAlreadyExistsException(AlreadyExistsException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpStatus> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpStatus> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccountStatusException.class, ExpiredJwtException.class, AccessDeniedException.class,
            SignatureException.class})
    public ResponseEntity<HttpStatus> handleForbiddenExceptions(RuntimeException ex) {
        log.error("Error message: {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
