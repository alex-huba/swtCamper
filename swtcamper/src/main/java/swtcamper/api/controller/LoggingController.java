package swtcamper.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.LoggingMessageDTO;
import swtcamper.api.contract.interfaces.ILoggingController;
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
  public void log(LoggingMessage loggingMessage) {
    loggingService.log(
      modelMapper.loggingMessageToLoggingMessageDTO(loggingMessage)
    );
  }

  @Override
  public List<LoggingMessageDTO> getAllLogMessages() {
    return modelMapper.LoggingMessagesToLoggingMessageDTOs(loggingService.getAllLogMessages());
  }

  @Override
  public List<LoggingMessageDTO> getLogForUser(User selectedUser) {
    return modelMapper.LoggingMessagesToLoggingMessageDTOs(loggingService.getLogForUser(selectedUser));
  }
}
