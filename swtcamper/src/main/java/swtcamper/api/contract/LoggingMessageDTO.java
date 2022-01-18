package swtcamper.api.contract;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import swtcamper.backend.entities.LoggingLevel;

public class LoggingMessageDTO {

  private long loggingMessageID;
  private LocalDateTime time;
  private LoggingLevel logLevel;
  private String loggingMessage;

  public LoggingMessageDTO(
    long id,
    LocalDateTime time,
    LoggingLevel logLvl,
    String message
  ) {
    this.loggingMessageID = id;
    this.time = time;
    this.logLevel = logLvl;
    this.loggingMessage = message;
  }

  public long getLoggingMessageID() {
    return loggingMessageID;
  }

  public void setLoggingMessageID(long loggingMessageID) {
    this.loggingMessageID = loggingMessageID;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
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

  public String toString() {
    return (
      time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm")) +
      "\t[" +
      logLevel +
      "]" +
      (logLevel.equals(LoggingLevel.INFO) ? "\t\t" : "\t") +
      loggingMessage +
      " (Log.ID: " +
      loggingMessageID +
      ")"
    );
  }
}
