package cz.cvut.ear.flashcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Unauthorized Exception
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@ResponseStatus(code = HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public static NotFoundException create(String resourceName, Object identifier) {
        return new NotFoundException(resourceName + " identified by " + identifier + " not found.");
    }
}
