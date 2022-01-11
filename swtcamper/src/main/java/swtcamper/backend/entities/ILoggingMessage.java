package swtcamper.backend.entities;

import java.time.LocalDateTime;

public interface ILoggingMessage {
  long getLoggingMessageID();
  void setLoggingMessageID(long loggingMessageID);
  void setTime(LocalDateTime time);
  LocalDateTime getTime();
  LoggingLevel getLogLevel();
  void setLogLevel(LoggingLevel logLevel);
  String getLoggingMessage();
  void setLoggingMessage(String loggingMessage);
}
