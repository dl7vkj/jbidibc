package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogFactory {
    private static final Level LOGLEVEL = Level.ALL;
    private static final boolean APPEND = false;

    private static FileHandler fh = null;

    public static Logger getLogger(String name) {
        Logger result = Logger.getLogger(name);

        if (fh != null) {
            result.addHandler(fh);
        }
        result.setLevel(LOGLEVEL);
        result.setUseParentHandlers(false);
        return result;
    }

    public static void setLogFile(String logFile) {
        if (logFile != null) {
            try {
                fh = new FileHandler(logFile, APPEND);
                fh.setFormatter(new MyFormatter());
                fh.setLevel(LOGLEVEL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
