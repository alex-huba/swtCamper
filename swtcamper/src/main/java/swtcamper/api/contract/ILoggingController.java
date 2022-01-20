package swtcamper.api.contract;

import swtcamper.backend.entities.User;

import java.util.List;

public interface ILoggingController {
  void log(LoggingMessageDTO loggingMessageDTO);
  List<LoggingMessageDTO> getAllLogMessages();
  List<LoggingMessageDTO> getLogForUser(User selectedUser);
}
