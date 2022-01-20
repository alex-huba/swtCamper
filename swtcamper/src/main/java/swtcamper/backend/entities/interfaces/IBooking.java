package swtcamper.backend.entities.interfaces;

import java.time.LocalDate;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;

public interface IBooking {
  Long getId();
  void setId(Long id);
  User getRenter();
  void setRenter(User renter);
  Offer getOffer();
  void setOffer(Offer offer);
  LocalDate getStartDate();
  void setStartDate(LocalDate startDate);
  LocalDate getEndDate();
  void setEndDate(LocalDate endDate);
  boolean isActive();
  void setActive(boolean active);
  boolean isRejected();
  void setRejected(boolean rejected);
}
