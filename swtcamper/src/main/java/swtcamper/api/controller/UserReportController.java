package swtcamper.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserReportDTO;
import swtcamper.api.contract.interfaces.IUserReportController;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.services.UserReportService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Controller
public class UserReportController implements IUserReportController {

  @Autowired
  private UserReportService userReportService;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public UserReportDTO create(UserReport userReport) {
    return modelMapper.userReportToUserReportDTO(
      userReportService.create(userReport)
    );
  }

  @Override
  public List<UserReportDTO> getAllUserReports() {
    return modelMapper.userReportsToUserReportDTOs(
      userReportService.getAllUserReports()
    );
  }

  @Override
  public UserReportDTO accept(long userID) throws GenericServiceException {
    return modelMapper.userReportToUserReportDTO(
      userReportService.acceptUserReport(userID)
    );
  }

  @Override
  public UserReportDTO reject(long userID) throws GenericServiceException {
    return modelMapper.userReportToUserReportDTO(
      userReportService.rejectUserReport(userID)
    );
  }
}
