package swtcamper.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.ILoggingController;
import swtcamper.api.contract.LoggingMessageDTO;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.LoggingService;

@Component
public class LoggingController implements ILoggingController {

  @Autowired
  private LoggingService loggingService;

  public void log(LoggingMessageDTO loggingMessageDTO) {
    loggingService.log(loggingMessageDTO);
  }

  public List<LoggingMessageDTO> getAllLogMessages() {
    return loggingService.getAllLogMessages();
  }

  public List<LoggingMessageDTO> getLogForUser(User selectedUser) {
    return getAllLogMessages()
      .stream()
      .filter(loggingMessage ->
        loggingMessage.getLoggingMessage().contains(selectedUser.getUsername())
      )
      .collect(Collectors.toList());
  }
}
