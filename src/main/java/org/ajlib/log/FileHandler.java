package org.ajlib.log;

import org.ajlib.util.Env;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class FileHandler implements LogHandler {
    private static final String KEY_LOG_DIR = "dir";
    private static final String KEY_LOG_NAME = "name";
    private static final String DEFAULT_LOG_NAME = "agent.log";
    private static final String KEY_MAX_FILES = "max-file";
    private static final int DEFAULT_MAX_FILES = 1;
    private final String logDir;
    private final String name;
    private final File logFile;
    private final int maxFiles;
    private volatile LocalDate lastRecordDate;
    private PrintStream output;

    public FileHandler(Map<String, Object> config) {
        final File parent = Env.agentBaseDir();
        logDir = (String) config.getOrDefault(KEY_LOG_DIR, new File(parent, "log").getAbsolutePath());
        name = (String) config.getOrDefault(KEY_LOG_NAME, DEFAULT_LOG_NAME);
        maxFiles = (int) config.getOrDefault(KEY_MAX_FILES, DEFAULT_MAX_FILES);
        logFile = new File(logDir, name);
    }


    @Override
    public void handleLog(LogRecord log) {
        try {
            getOutput(log).println(log.textify());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PrintStream getOutput(LogRecord log) throws IOException {
        final LocalDateTime time = log.getTime();
        final LocalDate logDate = time.toLocalDate();
        if (output == null) {
            output = new PrintStream(Files.newOutputStream(logFile.toPath(), StandardOpenOption.WRITE ,StandardOpenOption.APPEND, StandardOpenOption.CREATE), true);
            lastRecordDate = logDate;
        }
        if (logDate.isAfter(lastRecordDate)) {
            output.close();
            Files.move(logFile.toPath(),
                    new File(logFile.getName() + "."+ lastRecordDate.format(DateTimeFormatter.BASIC_ISO_DATE)).toPath(),
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            output = new PrintStream(Files.newOutputStream(logFile.toPath(), StandardOpenOption.WRITE ,StandardOpenOption.APPEND, StandardOpenOption.CREATE), true);
            lastRecordDate = logDate;
        }
        return output;
    }
}
