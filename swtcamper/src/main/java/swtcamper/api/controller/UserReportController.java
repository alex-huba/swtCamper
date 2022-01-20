package swtcamper.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import swtcamper.api.contract.interfaces.IUserReportController;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.services.UserReportService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Controller
public class UserReportController implements IUserReportController {

  @Autowired
  private UserReportService userReportService;

  public UserReport create(UserReport userReport) {
    return userReportService.create(userReport);
  }

  public List<UserReport> getAllUserReports() {
    return userReportService.getAllUserReports();
  }

  public UserReport accept(long userID) throws GenericServiceException {
    return userReportService.acceptUserReport(userID);
  }

  public UserReport reject(long userID) throws GenericServiceException {
    return userReportService.rejectUserReport(userID);
  }
}
