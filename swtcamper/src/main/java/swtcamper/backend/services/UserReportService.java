package swtcamper.backend.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.LoggingLevel;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.repositories.UserReportRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Service
public class UserReportService {

  @Autowired
  private UserReportRepository userReportRepository;

  @Autowired
  private LoggingService loggingService;

  @Autowired
  private UserController userController;

  @Autowired
  private ModelMapper modelMapper;

  public UserReport create(UserReport userReport) {
    UserReport reported = userReportRepository.save(
      new UserReport(
        userReport.getReporter(),
        userReport.getReportee(),
        userReport.getReportReason()
      )
    );
    loggingService.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s reported user %s. (UserReport-ID %s)",
            reported.getReporter().getUsername(),
            reported.getReportee().getUsername(),
            reported.getId()
          )
        )
      )
    );
    return reported;
  }

  public UserReport getUserReportById(long id) throws GenericServiceException {
    Optional<UserReport> userReportOptional = userReportRepository.findById(id);
    if (userReportOptional.isPresent()) {
      return userReportOptional.get();
    }
    throw new GenericServiceException(
      "There is no UserReport with this ID: " + id
    );
  }

  public List<UserReport> getAllUserReports() {
    return userReportRepository.findAll();
  }

  public UserReport acceptUserReport(long userID)
    throws GenericServiceException {
    UserReport userReport = getUserReportById(userID);
    userReport.setActive(false);
    loggingService.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "UserReport with ID %s was accepted by %s.",
            userID,
            userController.getLoggedInUser().getUsername()
          )
        )
      )
    );
    userController.blockUserById(userReport.getReportee().getId());
    return userReportRepository.save(userReport);
  }

  public UserReport rejectUserReport(long userID)
    throws GenericServiceException {
    UserReport userReport = getUserReportById(userID);
    userReport.setActive(false);
    loggingService.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "UserReport with ID %s was rejected by %s.",
            userID,
            userController.getLoggedInUser().getUsername()
          )
        )
      )
    );
    return userReportRepository.save(userReport);
  }

  public void delete(long id) {
    loggingService.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "UserReport with ID %s was deleted by %s.",
            id,
            userController.getLoggedInUser().getUsername()
          )
        )
      )
    );
    userReportRepository.deleteById(id);
  }
}
