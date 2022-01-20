package swtcamper.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.LoggingLevel;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.repositories.UserReportRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;
import java.util.Optional;

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

  /**
   * Creates a new {@link UserReport}
   * @param userReport UserReport to save
   * @return newly saved UserReport
   */
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

  /**
   * Gets a specific UserReport by its ID
   * @param id ID of the UserReport to get
   * @return found UserReport
   * @throws GenericServiceException if no UserReport could be found
   */
  public UserReport getUserReportById(long id) throws GenericServiceException {
    Optional<UserReport> userReportOptional = userReportRepository.findById(id);
    if (userReportOptional.isPresent()) {
      return userReportOptional.get();
    }
    throw new GenericServiceException(
      "There is no UserReport with this ID: " + id
    );
  }

  /**
   * Gets a list of all available UserReports
   * @return list of all available UserReports
   */
  public List<UserReport> getAllUserReports() {
    return userReportRepository.findAll();
  }

  /**
   * Sets the active state of a specific UserReport to false and blocks the reported User (reportee)
   * @param userReportID ID of the UserReport to accept
   * @return accepted UserReport
   * @throws GenericServiceException if there is no UserReport with this ID
   */
  public UserReport acceptUserReport(long userReportID)
    throws GenericServiceException {
    UserReport userReport = getUserReportById(userReportID);
    userReport.setActive(false);
    loggingService.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "UserReport with ID %s was accepted by %s. User %s gets blocked.",
            userReportID,
            userController.getLoggedInUser().getUsername(),
            userReport.getReportee()
          )
        )
      )
    );
    userController.blockUserById(userReport.getReportee().getId());
    return userReportRepository.save(userReport);
  }

  /**
   * Sets the active state of a specific UserReport to false
   * @param userReportID ID of the UserReport to reject
   * @return rejected UserReport
   * @throws GenericServiceException if there is no UserReport with this ID
   */
  public UserReport rejectUserReport(long userReportID)
    throws GenericServiceException {
    UserReport userReport = getUserReportById(userReportID);
    userReport.setActive(false);
    loggingService.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "UserReport with ID %s was rejected by %s.",
            userReportID,
            userController.getLoggedInUser().getUsername()
          )
        )
      )
    );
    return userReportRepository.save(userReport);
  }

  /**
   * Deletes a specific UserReport
   * @param id ID of the UserReport to delete
   */
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
