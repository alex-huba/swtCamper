package swtcamper.api.contract;

import java.util.List;
import swtcamper.backend.entities.User;

public interface ILoggingController {
  void log(LoggingMessageDTO loggingMessageDTO);
  List<LoggingMessageDTO> getAllLogMessages();
  List<LoggingMessageDTO> getLogForUser(User selectedUser);
}
