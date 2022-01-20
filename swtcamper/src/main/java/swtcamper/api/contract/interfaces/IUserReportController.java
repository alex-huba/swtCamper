package swtcamper.api.contract.interfaces;

import java.util.List;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.services.exceptions.GenericServiceException;

public interface IUserReportController {
  UserReport create(UserReport userReport);
  List<UserReport> getAllUserReports();
  UserReport accept(long id) throws GenericServiceException;
  UserReport reject(long id) throws GenericServiceException;
}
