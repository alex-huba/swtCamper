package swtcamper.backend.entities;

public interface IUserReport {
    Long getId();
    void setId(Long id);
    User getReporter();
    void setReporter(User reporter);
    User getReportee();
    void setReportee(User reportee);
    String getReportReason();
    void setReportReason(String reportReason);
}
