package cz.cvut.ear.flashcards.exception;

/** Base exception class
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
public abstract class BaseException extends RuntimeException {
    /** Constructor
     * Create empty exception
    */
    public BaseException() {

    }

    /** Constructor
     * Create exception with message
     * @param message Exception mesage
    */
    public BaseException(String message) {
        super(message);
    }

    /** Constructor
     * Create exception with message and cause
     * @param message Exception mesage
     * @param cause
    */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructor
     * Create exception with cause
     * @param cause
    */
    public BaseException(Throwable cause) {
        super(cause);
    }
}
