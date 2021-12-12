package swtcamper.backend.entities;

import java.util.ArrayList;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class Offer implements IOffer {

  @Id
  @GeneratedValue
  private long offerID;

  private OfferedObjectType offeredObjectType;

  @OneToOne
  private Vehicle offeredObject;

  private String title;
  private String location;
  private String contact;
  private String particularities;
  private ArrayList<Long> bookings;
  private long price;
  private boolean active;

  // Rental Conditions
  boolean minAge25;
  boolean borderCrossingAllowed;
  boolean depositInCash;

  public Offer(
    Vehicle vehicle,
    String title,
    String location,
    String contact,
    String particularities,
    long price,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash
  ) {
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObject = vehicle;
    this.bookings = new ArrayList<Long>();
    this.title = title;
    this.location = location;
    this.contact = contact;
    this.particularities = particularities;
    this.price = price;
    this.minAge25 = minAge25;
    this.borderCrossingAllowed = borderCrossingAllowed;
    this.depositInCash = depositInCash;
    this.active = true;
  }

  public Offer(
    Vehicle vehicle,
    ArrayList<Long> bookings,
    String title,
    String location,
    String contact,
    String particularities,
    long price,
    boolean active,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash
  ) {
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObject = vehicle;
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

  public Offer(Vehicle vehicle) {
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObject = vehicle;
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
  public Vehicle getOfferedObject() {
    return offeredObject;
  }

  @Override
  public void setOfferedObject(Vehicle offeredObject) {
    this.offeredObject = offeredObject;
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
  public String getTitle() {
    return title;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public String getContact() {
    return contact;
  }

  @Override
  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getParticularities() {
    return particularities;
  }

  public void setParticularities(String particularities) {
    this.particularities = particularities;
  }

  @Override
  public long getPrice() {
    return price;
  }

  @Override
  public void setPrice(long price) {
    this.price = price;
  }

  @Override
  public boolean isMinAge25() {
    return minAge25;
  }

  @Override
  public void setMinAge25(boolean minAge25) {
    this.minAge25 = minAge25;
  }

  @Override
  public boolean isBorderCrossingAllowed() {
    return borderCrossingAllowed;
  }

  @Override
  public void setBorderCrossingAllowed(boolean borderCrossingAllowed) {
    this.borderCrossingAllowed = borderCrossingAllowed;
  }

  @Override
  public boolean isDepositInCash() {
    return depositInCash;
  }

  @Override
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Offer offer = (Offer) o;
    return (
      offerID == offer.offerID &&
      price == offer.price &&
      active == offer.active &&
      minAge25 == offer.minAge25 &&
      borderCrossingAllowed == offer.borderCrossingAllowed &&
      depositInCash == offer.depositInCash &&
      offeredObjectType == offer.offeredObjectType &&
      Objects.equals(offeredObject, offer.offeredObject) &&
      Objects.equals(bookings, offer.bookings)
    );
  }
}
