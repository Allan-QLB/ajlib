package org.ajlib.log;

import java.io.PrintStream;

public class ConsoleHandler implements LogHandler {

    @Override
    public void handleLog(LogRecord log) {
        if (log != null) {
            getOutputString(log).println(log.textify());
        }
    }

    PrintStream getOutputString(LogRecord log) {
        if (log.getLevel() == Logger.Level.ERROR) {
            return System.err;
        } else {
            return System.out;
        }
    }
}
