package swtcamper.backend.entities.interfaces;

import java.time.LocalDateTime;
import swtcamper.backend.entities.LoggingLevel;

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
