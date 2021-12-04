package swtcamper.api.contract;

import java.util.ArrayList;
import swtcamper.backend.entities.Vehicle;

public class OfferDTO {

  private long offerID;
  private OfferedObjectTypeDTO offeredObjectType;
  private Vehicle offeredObject;
  private ArrayList<Long> bookings;
  private long price;
  boolean minAge25;
  boolean borderCrossingAllowed;
  boolean depositInCash;
  private boolean active;

  public OfferDTO(
    long offerID,
    OfferedObjectTypeDTO offeredObjectType,
    Vehicle offeredObject,
    ArrayList<Long> bookings,
    long price,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash,
    boolean active
  ) {
    this.offerID = offerID;
    this.offeredObjectType = offeredObjectType;
    this.offeredObject = offeredObject;
    this.bookings = bookings;
    this.price = price;
    this.minAge25 = minAge25;
    this.borderCrossingAllowed = borderCrossingAllowed;
    this.depositInCash = depositInCash;
    this.active = active;
  }

  public long getID() {
    return offerID;
  }

  public void setID(long offerID) {
    this.offerID = offerID;
  }

  public OfferedObjectTypeDTO getOfferedObjectType() {
    return offeredObjectType;
  }

  public void setOfferedObjectType(OfferedObjectTypeDTO offeredObjectType) {
    this.offeredObjectType = offeredObjectType;
  }

  public Vehicle getOfferedObject() {
    return offeredObject;
  }

  public void setOfferedObjectID(Vehicle offeredObject) {
    this.offeredObject = offeredObject;
  }

  public ArrayList<Long> getBookings() {
    return bookings;
  }

  public void setBookings(ArrayList<Long> bookings) {
    this.bookings = bookings;
  }

  public long getPrice() {
    return price;
  }

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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
