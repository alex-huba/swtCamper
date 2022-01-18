package swtcamper.backend.entities;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
public class Booking {

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
  boolean over;

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
    this.over = false;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getRenter() {
    return renter;
  }

  public void setRenter(User renter) {
    this.renter = renter;
  }

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isOver() {
    return over;
  }

  public void setOver(boolean rejected) {
    this.over = rejected;
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
