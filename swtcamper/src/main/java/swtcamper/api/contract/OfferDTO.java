package swtcamper.api.contract;

import java.util.ArrayList;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.Vehicle;

public class OfferDTO {

  private long offerID;
  private OfferedObjectTypeDTO offeredObjectType;
  private Vehicle offeredObject;
  private String title;
  private String location;
  private String contact;
  private String description;
  private ArrayList<Long> bookings;
  private long price;
  ArrayList<String> rentalConditions;
  private boolean active;

  public OfferDTO(
    long offerID,
    OfferedObjectTypeDTO offeredObjectType,
    Vehicle offeredObject,
    ArrayList<Long> bookings,
    String title,
    String location,
    String contact,
    String description,
    long price,
    ArrayList<String> rentalConditions,
    boolean active
  ) {
    this.offerID = offerID;
    this.offeredObjectType = offeredObjectType;
    this.offeredObject = offeredObject;
    this.bookings = bookings;
    this.title = title;
    this.location = location;
    this.contact = contact;
    this.description = description;
    this.price = price;
    this.rentalConditions = rentalConditions;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  public ArrayList<String> getRentalConditions() {
    return rentalConditions;
  }

  public void setRentalConditions(ArrayList<String> rentalConditions) {
    this.rentalConditions = rentalConditions;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public String toString() {
    return (
      "offerID: " +
      offerID +
      ", Fahrzeug: " +
      offeredObject.getVehicleFeatures().getMake() +
      " " +
      offeredObject.getVehicleFeatures().getModel() +
      " (" +
      offeredObject.getVehicleFeatures().getVehicleType() +
      ") (Bj. " +
      offeredObject.getVehicleFeatures().getYear() +
      ")" +
      ", Sitzplätze: " +
      offeredObject.getVehicleFeatures().getSeats() +
      ", Preis pro Tag: " +
      price +
      ", aktiv: " +
      active
    );
  }
}
