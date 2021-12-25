package swtcamper.backend.entities;

public interface ILoggingMessage {
  LoggingLevel getLogLevel();
  void setLogLevel(LoggingLevel logLevel);
  String getLoggingMessage();
  void setLoggingMessage(String loggingMessage);
}
