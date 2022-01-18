package swtcamper.backend.entities;

import javax.persistence.*;

@Entity
public class UserReport implements IUserReport {

  @Id
  @GeneratedValue
  private Long id;

  private boolean active;

  @ManyToOne
  private User reporter;

  @ManyToOne
  private User reportee;

  private String reportReason;

  public UserReport() {}

  public UserReport(User reporter, User reportee, String reportReason) {
    this.active = true;
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
      "UserReport{" +
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
