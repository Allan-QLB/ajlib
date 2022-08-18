package org.ajlib.log;

import org.ajlib.exception.LogException;
import org.ajlib.util.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public interface LogHandler {
    String buildLogText(@Nonnull LogRecord logRecord);
    PrintStream getOutput(@Nonnull LogRecord log) throws IOException;
    default void handleLog(@Nonnull LogRecord log) {
        try {
            getOutput(log).print(buildLogText(log));
        } catch (Throwable e) {
            throw new LogException("HandleLog failed " + log, e);
        }
    }
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
