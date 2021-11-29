package swtcamper.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Offer implements IOffer {

  @Id
  @GeneratedValue
  private Long offerID;

  private OfferedObjectType offeredObjectType;
  private Long offeredObjectID;
  private ArrayList<Long> bookings;
  private Long price;
  private String rentalConditions;
  private boolean active;

  public Offer(Vehicle vehicle, Long price, String rentalConditions) {
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObjectID = vehicle.getVehicleID();
    this.bookings = new ArrayList<Long>();
    this.price = price;
    this.rentalConditions = rentalConditions;
    this.active = true;
  }

  public Offer(Vehicle vehicle) {
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObjectID = vehicle.getVehicleID();
    this.bookings = new ArrayList<Long>();
    this.active = true;
  }

  public Offer() {
    super();
    this.bookings = new ArrayList<Long>();
  }

  @Override
  public Long getOfferID() {
    return offerID;
  }

  @Override
  public void setOfferID(Long offerID) {
    this.offerID = offerID;
  }

  @Override
  public OfferedObjectType getOfferedObjectType() {
    return offeredObjectType;
  }

  @Override
  public void setOfferedObjectType(OfferedObjectType offeredObjectType) {
    this.offeredObjectType = offeredObjectType;
  }

  @Override
  public Long getOfferedObjectID() {
    return offeredObjectID;
  }

  @Override
  public void setOfferedObjectID(Long offeredObjectID) {
    this.offeredObjectID = offeredObjectID;
  }

  @Override
  public ArrayList<Long> getBookings() {
    return bookings;
  }

  @Override
  public void setBookings(ArrayList<Long> bookings) {
    this.bookings = bookings;
  }

  @Override
  public Long getPrice() {
    return price;
  }

  @Override
  public void setPrice(Long price) {
    this.price = price;
  }

  @Override
  public String getRentalConditions() {
    return rentalConditions;
  }

  @Override
  public void setRentalConditions(String rentalConditions) {
    this.rentalConditions = rentalConditions;
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void setActive(boolean active) {
    this.active = active;
  }
}
