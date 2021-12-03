package swtcamper.backend.entities;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Offer implements IOffer {

  @Id
  @GeneratedValue
  private long offerID;

  private OfferedObjectType offeredObjectType;
  private long offeredObjectID;
  private ArrayList<Long> bookings;
  private long price;
  private boolean active;

  // Rental Conditions
  boolean minAge25;
  boolean borderCrossingAllowed;
  boolean depositInCash;

  public Offer(Vehicle vehicle, long price, boolean minAge25, boolean borderCrossingAllowed, boolean depositInCash) {
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObjectID = vehicle.getVehicleID();
    this.bookings = new ArrayList<Long>();
    this.price = price;
    this.minAge25= minAge25;
    this.borderCrossingAllowed = borderCrossingAllowed;
    this.depositInCash = depositInCash;
    this.active = true;
  }

  public Offer(Vehicle vehicle, ArrayList<Long> bookings, long price, boolean active, boolean minAge25, boolean borderCrossingAllowed, boolean depositInCash) {
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObjectID = vehicle.getVehicleID();
    this.bookings = bookings;
    this.price = price;
    this.minAge25 = minAge25;
    this.borderCrossingAllowed = borderCrossingAllowed;
    this.depositInCash = depositInCash;
    this.active = active;
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
  public long getOfferID() {
    return offerID;
  }

  @Override
  public void setOfferID(long offerID) {
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
  public long getOfferedObjectID() {
    return offeredObjectID;
  }

  @Override
  public void setOfferedObjectID(long offeredObjectID) {
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
  public long getPrice() {
    return price;
  }

  @Override
  public void setPrice(long price) {
    this.price = price;
  }

  public boolean isMinAge25() {
    return minAge25;
  }

  public void setMinAge25(boolean minAge25) {
    this.minAge25 = minAge25;
  }

  public boolean isBorderCrossingAllowed() {
    return borderCrossingAllowed;
  }

  public void setBorderCrossingAllowed(boolean borderCrossingAllowed) {
    this.borderCrossingAllowed = borderCrossingAllowed;
  }

  public boolean isDepositInCash() {
    return depositInCash;
  }

  public void setDepositInCash(boolean depositInCash) {
    this.depositInCash = depositInCash;
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
