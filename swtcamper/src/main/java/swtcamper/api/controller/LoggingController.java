package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.ILoggingController;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.LoggingService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoggingController implements ILoggingController {

  @Autowired
  private LoggingService loggingService;

  @Autowired
  private ModelMapper modelMapper;

  public void log(LoggingMessage loggingMessage) {
    loggingService.log(loggingMessage);
  }

  public List<LoggingMessage> getAllLogMessages() {
    return loggingService.getAllLogMessages();
  }

  public List<LoggingMessage> getLogForUser(User selectedUser) {
    return getAllLogMessages()
      .stream()
      .filter(loggingMessage ->
        loggingMessage.getLoggingMessage().contains(selectedUser.getUsername())
      )
      .collect(Collectors.toList());
  }
}
