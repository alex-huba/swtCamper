package swtcamper.backend.entities.interfaces;

import java.util.ArrayList;
import javafx.util.Pair;
import swtcamper.backend.entities.OfferedObjectType;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.Vehicle;

public interface IOffer {
  long getOfferID();

  void setOfferID(long offerID);

  User getCreator();

  void setCreator(User creator);

  OfferedObjectType getOfferedObjectType();

  void setOfferedObjectType(OfferedObjectType offeredObjectType);

  Vehicle getOfferedObject();

  void setOfferedObject(Vehicle offeredObject);

  ArrayList<Long> getBookings();

  void setBookings(ArrayList<Long> bookings);

  String getTitle();

  void setTitle(String title);

  String getLocation();

  void setLocation(String location);

  String getContact();

  void setContact(String contact);

  String getParticularities();

  void setParticularities(String particularities);

  long getPrice();

  void setPrice(long price);

  ArrayList<String> getRentalConditions();

  void setRentalConditions(ArrayList<String> rentalConditions);

  boolean isActive();

  void setActive(boolean active);

  ArrayList<Pair> getBlockedDates();
  void setBlockedDates(ArrayList<Pair> blockedDates);
  boolean isPromoted();
  void setPromoted(boolean promoted);
}
