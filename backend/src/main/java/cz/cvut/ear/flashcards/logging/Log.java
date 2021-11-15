package cz.cvut.ear.flashcards.logging;


import cz.cvut.ear.flashcards.interceptor.Interceptor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private FileOutputStream log;

    /**
     * Creates new logger.
     * @param name name of the log
     */
    public Log(String name) {
        try {
            log = new FileOutputStream(name, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *  Writes message to log output.
     * @param text string to be written to log
     */
    public void writeToLog(String text) {
        text += System.lineSeparator();
        try {
            log.write(text.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Interceptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ends logging and closes output.
     */
    public void closeLog() {
        try {
            log.close();
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

