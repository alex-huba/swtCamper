package swtcamper.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.LoggingMessageDTO;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;
import swtcamper.backend.repositories.LoggingRepository;

@Service
public class LoggingService {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private LoggingRepository loggingRepository;

  private static final Logger logger = LogManager.getLogger(
    LoggingService.class
  );

  /**
   * Logs a LoggingMessage to console and to the database
   * @param loggingMessage Message to be logged
   */
  public void log(LoggingMessageDTO loggingMessage) {
    loggingRepository.save(
      modelMapper.LoggingMessageDTOToLoggingMessage(loggingMessage)
    );

    switch (loggingMessage.getLogLevel()) {
      case INFO:
        logger.info(loggingMessage.getLoggingMessage());
        break;
      case WARNING:
        logger.warn(loggingMessage.getLoggingMessage());
        break;
      case ERROR:
        logger.error(loggingMessage.getLoggingMessage());
        break;
      default:
        logger.error(
          "[LoggingLevel could not be determined!] " +
          loggingMessage.getLoggingMessage()
        );
        break;
    }
  }

  /**
   * Gets a list of all available Log Messages
   * @return List of all available log messages
   */
  public List<LoggingMessageDTO> getAllLogMessages() {
    return modelMapper.LoggingMessagesToLoggingMessageDTOs(
      loggingRepository.findAll()
    );
  }

  /**
   * Get a list of all LogMessages related to a specific User
   * @param selectedUser specified User to look for
   * @return List of all LogMessages related to selectedUser
   */
  public List<LoggingMessageDTO> getLogForUser(User selectedUser) {
    return getAllLogMessages()
            .stream()
            .filter(loggingMessage ->
                    loggingMessage.getLoggingMessage().contains(selectedUser.getUsername())
            )
            .collect(Collectors.toList());
  }
}
