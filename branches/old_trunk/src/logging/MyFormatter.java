package logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {

    public String format(LogRecord record) {
        return formatMessage(record) + "\n";
    }
}