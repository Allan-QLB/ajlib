package org.ajlib.log;

import cn.hutool.core.lang.ansi.AnsiColor;
import cn.hutool.core.lang.ansi.AnsiEncoder;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintStream;
import java.util.function.Function;

public class ConsoleHandler implements LogHandler {

    private static final AnsiColor COLOR_TIME = AnsiColor.WHITE;
    private static final AnsiColor COLOR_CLASSNAME = AnsiColor.CYAN;
    private static final AnsiColor COLOR_NONE = AnsiColor.DEFAULT;

    private static Function<Logger.Level, AnsiColor> colorFactory = (level -> {
        switch (level) {
            case DEBUG:
                return AnsiColor.GREEN;
            case INFO:
                return AnsiColor.BLUE;
            case WARN:
                return AnsiColor.YELLOW;
            case ERROR:
                return AnsiColor.RED;
            default:
                return COLOR_NONE;
        }
    });

    @Override
    public void handleLog(LogRecord log) {
        if (log != null) {
            getOutputString(log).print(textify(log));
        }
    }

    @Override
    public String textify(LogRecord logRecord) {
        final String template = AnsiEncoder.encode(COLOR_TIME, "[%s]", colorFactory.apply(logRecord.getLevel()), "[%-5s]%s", COLOR_CLASSNAME, "%-30s: ", COLOR_NONE, "%s%n");
        final FormattingTuple formattedMsg = MessageFormatter.arrayFormat(logRecord.getMessage(), logRecord.getArguments());
        return String.format(template, logRecord.getTime(),
                logRecord.getLevel(),
                " ",
                logRecord.getLogName(),
                formattedMsg.getMessage() + textifyThrowable(formattedMsg.getThrowable()));
    }

    PrintStream getOutputString(LogRecord log) {
        if (log.getLevel() == Logger.Level.ERROR) {
            return System.err;
        } else {
            return System.out;
        }
    }
}
