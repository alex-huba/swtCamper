package swtcamper.api.contract;

import swtcamper.backend.entities.LoggingMessage;

import java.util.List;

public interface ILoggingController {
    List<LoggingMessage> getAllLogMessages();
    void log(LoggingMessage loggingMessage);
}
