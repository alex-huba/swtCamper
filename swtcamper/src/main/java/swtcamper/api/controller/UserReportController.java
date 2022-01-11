package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import swtcamper.api.contract.IUserReportController;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.services.UserReportService;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;

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

    public UserReport close(long id) throws GenericServiceException {
        return userReportService.closeUserReport(id);
    }
}
