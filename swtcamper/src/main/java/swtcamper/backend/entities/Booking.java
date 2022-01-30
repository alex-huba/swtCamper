package swtcamper.backend.entities;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import swtcamper.backend.entities.interfaces.IBooking;

@Entity
public class Booking implements IBooking {

  @Id
  @GeneratedValue
  Long id;

  @ManyToOne
  User renter;

  @ManyToOne
  Offer offer;

  LocalDate startDate;
  LocalDate endDate;
  boolean active;
  boolean rejected;

  public Booking() {}

  public Booking(
    User renter,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate
  ) {
    this.renter = renter;
    this.offer = offer;
    this.startDate = startDate;
    this.endDate = endDate;
    this.active = false;
    this.rejected = false;
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
  public User getRenter() {
    return renter;
  }

  @Override
  public void setRenter(User renter) {
    this.renter = renter;
  }

  @Override
  public Offer getOffer() {
    return offer;
  }

  @Override
  public void setOffer(Offer offer) {
    this.offer = offer;
  }

  @Override
  public LocalDate getStartDate() {
    return startDate;
  }

  @Override
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  @Override
  public LocalDate getEndDate() {
    return endDate;
  }

  @Override
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
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
  public boolean isRejected() {
    return rejected;
  }

  @Override
  public void setRejected(boolean rejected) {
    this.rejected = rejected;
  }

  @Override
  public String toString() {
    return (
      "Booking{" +
      "id=" +
      id +
      ", renter=" +
      renter +
      ", offer=" +
      offer +
      ", startDate=" +
      startDate +
      ", endDate=" +
      endDate +
      ", active=" +
      active +
      '}'
    );
  }
}
