package swtcamper.api.contract;

import swtcamper.backend.entities.UserReport;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;

public interface IUserReportController {
    UserReport create(UserReport userReport);
    List<UserReport> getAllUserReports();
    UserReport accept(long id) throws GenericServiceException;
    UserReport reject(long id) throws GenericServiceException;
}
