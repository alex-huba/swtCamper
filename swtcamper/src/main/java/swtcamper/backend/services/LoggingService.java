package swtcamper.backend.services;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.LoggingLevel;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.repositories.LoggingRepository;

@Service
public class LoggingService {
    @Autowired
    private LoggingRepository loggingRepository;

    private static Logger logger = LogManager.getLogger(LoggingService.class);

    public void log(LoggingMessage loggingMessage) {
        loggingRepository.save(loggingMessage);

        if (loggingMessage.getLogLevel().equals(LoggingLevel.INFO)) {
            logger.info(loggingMessage.getLoggingMessage());
        } else if (loggingMessage.getLogLevel().equals(LoggingLevel.WARNING)) {
            logger.warn(loggingMessage.getLoggingMessage());
        } else if (loggingMessage.getLogLevel().equals(LoggingLevel.ERROR)) {
            logger.error(loggingMessage.getLoggingMessage());
        }
    }
}
