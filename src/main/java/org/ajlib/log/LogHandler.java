package org.ajlib.log;

import org.ajlib.util.StringUtil;

import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;

public interface LogHandler {
    void handleLog(LogRecord log);
    String textify(LogRecord logRecord);

    default String textifyThrowable(@Nullable Throwable throwable) {
        if (throwable == null) {
            return StringUtil.empty();
        }
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        pw.println();
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
