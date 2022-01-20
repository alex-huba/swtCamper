package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.ILoggingController;
import swtcamper.api.contract.LoggingMessageDTO;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.LoggingService;

import java.util.List;

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
    return loggingService.getLogForUser(selectedUser);
  }
}
