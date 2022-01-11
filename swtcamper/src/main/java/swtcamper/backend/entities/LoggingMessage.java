package swtcamper.backend.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LoggingMessage implements ILoggingMessage {

  @Id
  @GeneratedValue
  private long loggingMessageID;

  private LocalDateTime time;
  private LoggingLevel logLevel;
  private String loggingMessage;

  public LoggingMessage() {
  }

  /**
   * LoggingMessage needed to send information to LoggingService
   *
   * @param logLvl  LoggingLevel to specify the severity of the message
   * @param message Message that shall be logged
   */
  public LoggingMessage(LoggingLevel logLvl, String message) {
    this.time = LocalDateTime.now();

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

  @Override
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
