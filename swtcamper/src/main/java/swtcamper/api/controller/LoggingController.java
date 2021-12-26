package swtcamper.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.ILoggingController;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.LoggingService;

@Component
public class LoggingController implements ILoggingController {

  @Autowired
  private LoggingService loggingService;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public List<LoggingMessage> getAllLogMessages() {
    return loggingService.getAllLogMessages();
  }

  public void log(LoggingMessage loggingMessage) {
    loggingService.log(loggingMessage);
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
