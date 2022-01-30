package swtcamper.backend.entities.interfaces;

import swtcamper.backend.entities.User;

public interface IUserReport {
  Long getId();

  void setId(Long id);

  boolean isActive();

  void setActive(boolean active);

  User getReporter();

  void setReporter(User reporter);

  User getReportee();

  void setReportee(User reportee);

  String getReportReason();

  void setReportReason(String reportReason);
}
