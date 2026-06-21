package ec.edu.espe.banquito.party.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final ZoneId APP_ZONE = ZoneId.of("America/Guayaquil");
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCustomerNotFound(CustomerNotFoundException exception) {
        return Map.of(
                TIMESTAMP, LocalDateTime.now(APP_ZONE),
                STATUS, 404,
                ERROR, "NOT_FOUND",
                MESSAGE, exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleInvalidCredentials(InvalidCredentialsException exception) {
        return Map.of(
                TIMESTAMP, LocalDateTime.now(APP_ZONE),
                STATUS, 401,
                ERROR, "UNAUTHORIZED",
                MESSAGE, exception.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException exception) {
        return Map.of(
                TIMESTAMP, LocalDateTime.now(APP_ZONE),
                STATUS, 400,
                ERROR, "BAD_REQUEST",
                MESSAGE, exception.getMessage()
        );
    }
}