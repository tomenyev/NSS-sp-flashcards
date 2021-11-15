package cz.cvut.ear.flashcards.exception;

/** Persistence Exception
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
public class PersistenceException extends BaseException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
