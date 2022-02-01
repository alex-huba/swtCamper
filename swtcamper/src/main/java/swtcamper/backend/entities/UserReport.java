package swtcamper.backend.entities;

import javax.persistence.*;

import swtcamper.backend.entities.interfaces.IUserReport;

@Entity
public class UserReport implements IUserReport {

  @Id
  @GeneratedValue
  private Long id;

  private boolean active;

  @ManyToOne(cascade = {CascadeType.MERGE})
  private User reporter;

  @ManyToOne(cascade = {CascadeType.MERGE})
  private User reportee;

  private String reportReason;

  public UserReport() {}

  public UserReport(User reporter, User reportee, String reportReason) {
    this.active = true;
    this.reporter = reporter;
    this.reportee = reportee;
    this.reportReason = reportReason;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public User getReporter() {
    return reporter;
  }

  @Override
  public void setReporter(User reporter) {
    this.reporter = reporter;
  }

  @Override
  public User getReportee() {
    return reportee;
  }

  @Override
  public void setReportee(User reportee) {
    this.reportee = reportee;
  }

  @Override
  public String getReportReason() {
    return reportReason;
  }

  @Override
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
