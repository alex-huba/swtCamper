package swtcamper.api.contract.interfaces;

import java.util.List;
import swtcamper.api.contract.UserReportDTO;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.services.exceptions.GenericServiceException;

public interface IUserReportController {
  UserReportDTO create(UserReport userReport);

  List<UserReportDTO> getAllUserReports();

  UserReportDTO accept(long id) throws GenericServiceException;

  UserReportDTO reject(long id) throws GenericServiceException;
}
