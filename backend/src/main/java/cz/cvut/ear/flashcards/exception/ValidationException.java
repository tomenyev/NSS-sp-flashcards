package cz.cvut.ear.flashcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Signifies that invalid data have been provided to the application.
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class ValidationException extends BaseException {

    public ValidationException(String message) {
        super(message);
    }
}
