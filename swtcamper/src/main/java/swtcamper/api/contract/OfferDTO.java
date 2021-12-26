package swtcamper.api.contract;

import java.util.ArrayList;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.Vehicle;

public class OfferDTO {

  private long offerID;
  private User creator;
  private OfferedObjectTypeDTO offeredObjectType;
  private Vehicle offeredObject;
  private String title;
  private String location;
  private String contact;
  private String particularities;
  private ArrayList<Long> bookings;
  private long price;
  boolean minAge25;
  boolean borderCrossingAllowed;
  boolean depositInCash;
  private boolean active;

  public OfferDTO(
    long offerID,
    User creator,
    OfferedObjectTypeDTO offeredObjectType,
    Vehicle offeredObject,
    ArrayList<Long> bookings,
    String title,
    String location,
    String contact,
    String particularities,
    long price,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash,
    boolean active
  ) {
    this.offerID = offerID;
    this.creator = creator;
    this.offeredObjectType = offeredObjectType;
    this.offeredObject = offeredObject;
    this.bookings = bookings;
    this.title = title;
    this.location = location;
    this.contact = contact;
    this.particularities = particularities;
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

  public User getCreator() {
    return this.creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
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

  public String getParticularities() {
    return particularities;
  }

  public void setParticularities(String particularities) {
    this.particularities = particularities;
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

  @Override
  public String toString() {
    return "OfferDTO{" +
            "offerID=" + offerID +
            ", creator=" + creator.getUsername() +
            ", offeredObjectType=" + offeredObjectType +
            ", offeredObject=" + offeredObject +
            ", title='" + title + '\'' +
            ", location='" + location + '\'' +
            ", contact='" + contact + '\'' +
            ", particularities='" + particularities + '\'' +
            ", bookings=" + bookings +
            ", price=" + price +
            ", minAge25=" + minAge25 +
            ", borderCrossingAllowed=" + borderCrossingAllowed +
            ", depositInCash=" + depositInCash +
            ", active=" + active +
            '}';
  }
}
