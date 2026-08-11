package lldinterview.loggersystem;

public class LoggerSystemMain {
    public static void main(String[] args) {
        Appender consoleAppender = new ConsoleAppender();
        Logger logger1 = new Logger(consoleAppender);
        logger1.info("This is an info message");
        logger1.error("This is an error message");

        Appender fileAppender = new FileAppender();
        Logger logger2 = new Logger(fileAppender);
        logger2.info("This is an info message");
        logger2.error("This is an error message");

        Appender databaseAppender = new DatabaseAppender();
        Logger logger3 = new Logger(databaseAppender);
        logger3.info("This is an info message");
        logger3.error("This is an error message");
    }
}
class Logger{
    private Appender appender;
    public Logger(Appender appender){
        this.appender = appender;
    }
    public void info(String message){
        appender.append(new LoggerMessage(message, "INFO", System.currentTimeMillis() + ""));
    }
    public void error(String message){
        appender.append(new LoggerMessage(message, "ERROR", System.currentTimeMillis() + ""));
    }
}
interface Appender{
    public void append(LoggerMessage message);
}
class ConsoleAppender implements Appender{
    @Override
    public void append(LoggerMessage message) {
        System.out.println("Writing to console: " + message);
        System.out.println(message);
    }
}
class FileAppender implements Appender{
    @Override
    public void append(LoggerMessage message) {
        //write to file
        System.out.println("Writing to file: " + message);
        System.out.println(message);
    }
}
class DatabaseAppender implements Appender{
    @Override
    public void append(LoggerMessage message) {
        //write to database
        System.out.println("Writing to database: " + message);
        System.out.println(message);
    }
}
class LoggerMessage{
    private String message;
    private String logLevel;
    private String timestamp;
    public LoggerMessage(String message, String logLevel, String timestamp){
        this.message = message;
        this.logLevel = logLevel;
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "[" + timestamp + "] " + logLevel + ": " + message;
    }
}
