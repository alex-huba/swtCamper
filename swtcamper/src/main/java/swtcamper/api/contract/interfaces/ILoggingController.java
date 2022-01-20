package swtcamper.api.contract.interfaces;

import java.util.List;
import swtcamper.api.contract.LoggingMessageDTO;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;

public interface ILoggingController {
  void log(LoggingMessage loggingMessage);
  List<LoggingMessageDTO> getAllLogMessages();
  List<LoggingMessageDTO> getLogForUser(User selectedUser);
}
