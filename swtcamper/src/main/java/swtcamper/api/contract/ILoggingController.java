package swtcamper.api.contract;

import java.util.List;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;

public interface ILoggingController {
  void log(LoggingMessage loggingMessage);
  List<LoggingMessage> getAllLogMessages();
  List<LoggingMessage> getLogForUser(User selectedUser);
}
