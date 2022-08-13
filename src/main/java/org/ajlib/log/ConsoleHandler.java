package org.ajlib.log;

import cn.hutool.core.lang.ansi.AnsiColor;
import cn.hutool.core.lang.ansi.AnsiEncoder;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.function.Function;

public class ConsoleHandler implements LogHandler {
    private static final AnsiColor COLOR_TIME = AnsiColor.WHITE;
    private static final AnsiColor COLOR_CLASSNAME = AnsiColor.CYAN;
    private static final AnsiColor COLOR_NONE = AnsiColor.DEFAULT;

    private static final Function<Logger.Level, AnsiColor> COLOR_FACTORY = (level -> {
        switch (level) {
            case DEBUG:
                return AnsiColor.BRIGHT_GREEN;
            case INFO:
                return AnsiColor.BRIGHT_BLUE;
            case WARN:
                return AnsiColor.BRIGHT_YELLOW;
            case ERROR:
                return AnsiColor.RED;
            default:
                return COLOR_NONE;
        }
    });

    @Override
    public void handleLog(@Nonnull LogRecord log) {
        getOutput(log).print(buildLogText(log));
    }

    @Override
    public String buildLogText(@Nonnull LogRecord logRecord) {
        final String format = AnsiEncoder.encode(
                COLOR_TIME, "[%s]",
                COLOR_FACTORY.apply(logRecord.getLevel()), "[%s]%s",
                COLOR_CLASSNAME, "%-30s: ",
                COLOR_NONE, "%s%n");
        final FormattingTuple formattedMsg = MessageFormatter.arrayFormat(logRecord.getMessage(), logRecord.getArguments());
        return String.format(format, logRecord.getTime(),
                logRecord.getLevel(),
                " ",
                logRecord.getLogName(),
                formattedMsg.getMessage() + textifyThrowable(formattedMsg.getThrowable()));
    }

    @Override
    public PrintStream getOutput(@Nonnull LogRecord log) {
        if (log.getLevel() == Logger.Level.ERROR) {
            return System.err;
        } else {
            return System.out;
        }
    }
}
