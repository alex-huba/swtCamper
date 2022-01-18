package swtcamper.backend.entities;

import java.util.ArrayList;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Offer implements IOffer {

  @Id
  @GeneratedValue
  private long offerID;

  @OneToOne
  private User creator;

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
  private boolean promoted;

  // Rental Conditions
  private ArrayList<String> rentalConditions;

  public Offer(
    User creator,
    Vehicle vehicle,
    String title,
    String location,
    String contact,
    String particularities,
    long price,
    ArrayList<String> rentalConditions
  ) {
    this.creator = creator;
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObject = vehicle;
    this.bookings = new ArrayList<Long>();
    this.title = title;
    this.location = location;
    this.contact = contact;
    this.particularities = particularities;
    this.price = price;
    this.rentalConditions = rentalConditions;
    this.active = true;
  }

  public Offer(
    User creator,
    Vehicle vehicle,
    ArrayList<Long> bookings,
    String title,
    String location,
    String contact,
    String particularities,
    long price,
    boolean active,
    ArrayList<String> rentalConditions
  ) {
    this.creator = creator;
    this.offeredObjectType = OfferedObjectType.VEHICLE;
    this.offeredObject = vehicle;
    this.bookings = bookings;
    this.title = title;
    this.location = location;
    this.contact = contact;
    this.particularities = particularities;
    this.price = price;
    this.rentalConditions = rentalConditions;
    this.active = active;
  }

  public Offer(User creator, Vehicle vehicle) {
    this.creator = creator;
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
  public User getCreator() {
    return this.creator;
  }

  @Override
  public void setCreator(User creator) {
    this.creator = creator;
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

  public ArrayList<String> getRentalConditions() {
    return rentalConditions;
  }

  public void setRentalConditions(ArrayList<String> rentalConditions) {
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

  public boolean isPromoted() {
    return promoted;
  }

  public void setPromoted(boolean promoted) {
    this.promoted = promoted;
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
      rentalConditions == offer.rentalConditions &&
      offeredObjectType == offer.offeredObjectType &&
      Objects.equals(offeredObject, offer.offeredObject) &&
      Objects.equals(bookings, offer.bookings)
    );
  }

  @Override
  public String toString() {
    return (
      "Offer{" +
      "offerID=" +
      offerID +
      ", creator=" +
      creator.getUsername() +
      ", offeredObjectType=" +
      offeredObjectType +
      ", offeredObject=" +
      offeredObject +
      ", title='" +
      title +
      '\'' +
      ", location='" +
      location +
      '\'' +
      ", contact='" +
      contact +
      '\'' +
      ", particularities='" +
      particularities +
      '\'' +
      ", bookings=" +
      bookings +
      ", price=" +
      price +
      ", active=" +
      active +
      '}'
    );
  }
}
