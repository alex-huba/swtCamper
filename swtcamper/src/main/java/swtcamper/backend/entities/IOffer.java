package swtcamper.backend.entities;

import java.util.ArrayList;

public interface IOffer {
  public long getOfferID();

  public void setOfferID(long offerID);

  User getCreator();

  void setCreator(User creator);

  public OfferedObjectType getOfferedObjectType();

  public void setOfferedObjectType(OfferedObjectType offeredObjectType);

  public Vehicle getOfferedObject();

  public void setOfferedObject(Vehicle offeredObject);

  public ArrayList<Long> getBookings();

  public void setBookings(ArrayList<Long> bookings);

  public String getTitle();

  public void setTitle(String title);

  public String getLocation();

  public void setLocation(String location);

  public String getContact();

  public void setContact(String contact);

  public String getParticularities();

  public void setParticularities(String particularities);

  public long getPrice();

  public void setPrice(long price);

  public boolean isMinAge25();

  public void setMinAge25(boolean minAge25);

  public boolean isBorderCrossingAllowed();

  public void setBorderCrossingAllowed(boolean borderCrossingAllowed);

  public boolean isDepositInCash();

  public void setDepositInCash(boolean depositInCash);

  public boolean isActive();

  public void setActive(boolean active);
}
