package narciso.guilherme.github.profile.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  public record ErrorResponse(int status, String message) {}

  public record ValidationErrorResponse(int status, List<String> errors) {}

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(field -> field.getField() + ": " + field.getDefaultMessage())
        .toList();
    log.warn("Validation failed: {}", errors);
    return ResponseEntity.badRequest().body(new ValidationErrorResponse(400, errors));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
    log.warn("Invalid login attempt");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponse(401, "Invalid email or password"));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException ex) {
    log.warn("Conflict: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("Bad request: {}", ex.getMessage());
    return ResponseEntity.badRequest().body(new ErrorResponse(400, ex.getMessage()));
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<ErrorResponse> handleIO(IOException ex) {
    log.error("Failed to process image", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(500, "Failed to process image"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    log.error("Unexpected error", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(500, "Internal server error"));
  }
}
