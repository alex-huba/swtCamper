package swtcamper.api.contract;

import java.util.List;
import swtcamper.backend.entities.LoggingMessage;

public interface ILoggingController {
  List<LoggingMessage> getAllLogMessages();
  void log(LoggingMessage loggingMessage);
}
