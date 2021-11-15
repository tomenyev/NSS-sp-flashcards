package cz.cvut.ear.flashcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Not Found Exception
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static NotFoundException create(String resourceName, Object identifier) {
        return new NotFoundException(resourceName + " identified by " + identifier + " not found.");
    }
}
