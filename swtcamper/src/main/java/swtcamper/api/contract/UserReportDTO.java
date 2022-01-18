package swtcamper.api.contract;

import swtcamper.backend.entities.User;

public class UserReportDTO {

  private Long id;

  private boolean active;

  private User reporter;

  private User reportee;

  private String reportReason;

  public UserReportDTO(
    long id,
    boolean active,
    User reporter,
    User reportee,
    String reportReason
  ) {
    this.id = id;
    this.active = active;
    this.reporter = reporter;
    this.reportee = reportee;
    this.reportReason = reportReason;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public User getReporter() {
    return reporter;
  }

  public void setReporter(User reporter) {
    this.reporter = reporter;
  }

  public User getReportee() {
    return reportee;
  }

  public void setReportee(User reportee) {
    this.reportee = reportee;
  }

  public String getReportReason() {
    return reportReason;
  }

  public void setReportReason(String reportReason) {
    this.reportReason = reportReason;
  }

  @Override
  public String toString() {
    return (
      "UserReportDTO{" +
      "id=" +
      id +
      ", active=" +
      active +
      ", reporter=" +
      reporter +
      ", reportee=" +
      reportee +
      ", reportReason='" +
      reportReason +
      '\'' +
      '}'
    );
  }
}
