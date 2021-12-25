package swtcamper.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class LoggingMessage implements ILoggingMessage {
    @Id
    @GeneratedValue
    private long loggingMessageID;

    private LocalDateTime time;
    private LoggingLevel logLevel;
    private String loggingMessage;

    public LoggingMessage() {
        this.time = LocalDateTime.now();
        this.logLevel = LoggingLevel.ERROR;
        this.loggingMessage = "Unauthorized use of LoggingMessage! Please formally instantiate with LoggingLevel and log-message when using.";
    }

    /**
     * LoggingMessage needed to send information to LoggingService
     * @param logLvl LoggingLevel to specify the severity of the message
     * @param message Message that shall be logged
     */
    public LoggingMessage(LoggingLevel logLvl, String message) {
        this.time = LocalDateTime.now();

        this.logLevel = logLvl;
        this.loggingMessage = message;
    }

    public LoggingLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LoggingLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getLoggingMessage() {
        return loggingMessage;
    }

    public void setLoggingMessage(String loggingMessage) {
        this.loggingMessage = loggingMessage;
    }

    @Override
    public String toString() {
        return "LoggingMessage{" +
                "loggingMessageID=" + loggingMessageID +
                ", logLevel=" + logLevel +
                ", loggingMessage='" + loggingMessage + '\'' +
                ", time=" + time +
                '}';
    }
}
