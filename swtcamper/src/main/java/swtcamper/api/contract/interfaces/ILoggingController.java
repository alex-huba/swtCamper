package swtcamper.api.contract.interfaces;

import java.util.List;
import swtcamper.api.contract.LoggingMessageDTO;
import swtcamper.backend.entities.User;

public interface ILoggingController {
  void log(LoggingMessageDTO loggingMessageDTO);
  List<LoggingMessageDTO> getAllLogMessages();
  List<LoggingMessageDTO> getLogForUser(User selectedUser);
}
