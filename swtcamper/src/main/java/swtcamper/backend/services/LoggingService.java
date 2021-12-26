package swtcamper.backend.services;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.repositories.LoggingRepository;

@Service
public class LoggingService {

  @Autowired
  private LoggingRepository loggingRepository;

  private static Logger logger = LogManager.getLogger(LoggingService.class);

  /**
   * Logs a LoggingMessage to console and to the database
   * @param loggingMessage Message to be logged
   */
  public void log(LoggingMessage loggingMessage) {
    loggingRepository.save(loggingMessage);

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

  public List<LoggingMessage> getAllLogMessages() {
    return loggingRepository.findAll();
  }
}
