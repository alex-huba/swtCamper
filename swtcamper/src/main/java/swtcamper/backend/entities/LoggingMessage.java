package swtcamper.backend.entities;

import swtcamper.api.contract.ILoggingMessage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LoggingMessage implements ILoggingMessage {
    @Id
    @GeneratedValue
    private long loggingMessageID;

    private LoggingLevel logLevel;
    private String loggingMessage;

    public LoggingMessage() {}

    public LoggingMessage(LoggingLevel logLvl, String message) {
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
                '}';
    }
}
