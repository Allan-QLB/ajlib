package org.ajlib.log;

import org.ajlib.util.StringUtil;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

public class LogRecord {
    private static final String HEAD = "%s %s %s ";
    private final Logger.Level level;
    private final String logName;
    private final String message;
    private final Object[] arguments;
    private final LocalDateTime time;

    public LogRecord(Logger.Level level, String name, String message, Object... arguments) {
        this.level = level;
        this.logName = name;
        this.message = message;
        this.arguments = arguments;
        this.time = LocalDateTime.now();
    }

    public String textify() {
        final FormattingTuple formatted = MessageFormatter.arrayFormat(this.message, arguments);
        return header() + formatted.getMessage() + throwableStr(formatted.getThrowable());
    }

    private String throwableStr(@Nullable Throwable throwable) {
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

    private String header() {
         return String.format(HEAD, time, level, logName);
    }

    public Logger.Level getLevel() {
        return level;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
