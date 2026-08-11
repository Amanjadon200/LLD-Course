package lldinterview.loggersystem;

public class LoggerSystemMain {
    public static void main(String[] args) {
        AppenderStrategy consoleAppender = new ConsoleAppender();
        AppenderStrategy fileAppender = new FileAppender();
        AppenderStrategy databaseAppender = new DatabaseAppender();
        LoggerConfig devConfig = new LoggerConfig(LogLevel.INFO.getPriority(), LogLevel.INFO,
                new AppenderStrategy[] { consoleAppender, fileAppender, databaseAppender });
        Logger logger1 = new Logger(devConfig);
        logger1.info("This is an info message");
        logger1.error("This is an error message");

        LoggerConfig fileConfig = new LoggerConfig(LogLevel.INFO.getPriority(), LogLevel.INFO,
                new AppenderStrategy[] { fileAppender });
        Logger logger2 = new Logger(fileConfig);
        logger2.info("This is an info message");
        logger2.error("This is an error message");

        LoggerConfig databaseConfig = new LoggerConfig(LogLevel.INFO.getPriority(), LogLevel.INFO,
                new AppenderStrategy[] { databaseAppender });
        Logger logger3 = new Logger(databaseConfig);
        logger3.info("This is an info message");
        logger3.error("This is an error message");
    }
}

class Logger {
    private LoggerConfig config;

    public Logger(LoggerConfig config) {
        this.config = config;
    }

    public void info(String message) {
        if (LogLevel.INFO.getPriority() >= config.priority) {
            for (AppenderStrategy app : config.appender) {
                app.append(new LoggerMessage(message, LogLevel.INFO, System.currentTimeMillis()));
            }
        }
    }

    public void error(String message) {
        if (LogLevel.ERROR.getPriority() >= config.priority) {
            for (AppenderStrategy app : config.appender) {
                app.append(new LoggerMessage(message, LogLevel.ERROR, System.currentTimeMillis()));
            }
        }
    }
}

interface AppenderStrategy {
    public void append(LoggerMessage message);
}

class ConsoleAppender implements AppenderStrategy {
    @Override
    public void append(LoggerMessage message) {
        System.out.println("Writing to console: " + message);
        System.out.println(message);
    }
}

class FileAppender implements AppenderStrategy {
    @Override
    public void append(LoggerMessage message) {
        // write to file
        System.out.println("Writing to file: " + message);
        System.out.println(message);
    }
}

class DatabaseAppender implements AppenderStrategy {
    @Override
    public void append(LoggerMessage message) {
        // write to database
        System.out.println("Writing to database: " + message);
        System.out.println(message);
    }
}

class LoggerMessage {
    private final String message;
    private final LogLevel logLevel;
    private final long timestamp;

    public LoggerMessage(String message, LogLevel logLevel, long timestamp) {
        this.message = message;
        this.logLevel = logLevel;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + logLevel + ": " + message;
    }
}

class LoggerConfig {
    int priority;
    LogLevel logLevel;
    AppenderStrategy appender[];

    public LoggerConfig(int priority, LogLevel logLevel, AppenderStrategy appender[]) {
        this.priority = priority;
        this.logLevel = logLevel;
        this.appender = appender;
    }
}

enum LogLevel {
    INFO(2), ERROR(4), DEBUG(1), WARN(3);

    private int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}