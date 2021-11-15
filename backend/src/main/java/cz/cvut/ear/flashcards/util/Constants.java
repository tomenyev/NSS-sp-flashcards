package cz.cvut.ear.flashcards.util;

import cz.cvut.ear.flashcards.logging.Log;
import cz.cvut.ear.flashcards.model.ERole;

/**
 * Application constants
 */
public class Constants {

    public static final ERole USER = ERole.ROLE_USER;

    public static final ERole ADMIN = ERole.ROLE_ADMIN;

    public static final ERole GUEST = ERole.ROLE_GUSET;

    public static final Log badAccessLogger = new Log("logging/badAccess.log");

    public static final Log authLogger = new Log("logging/authentication.log");

}
