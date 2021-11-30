package swtcamper.backend.entities;

import java.util.ArrayList;

public interface IOffer {
  public Long getOfferID();

  public void setOfferID(Long offerID);

  public OfferedObjectType getOfferedObjectType();

  public void setOfferedObjectType(OfferedObjectType offeredObjectType);

  public Long getOfferedObjectID();

  public void setOfferedObjectID(Long offeredObjectID);

  public ArrayList<Long> getBookings();

  public void setBookings(ArrayList<Long> bookings);

  public Long getPrice();

  public void setPrice(Long price);

  public String getRentalConditions();

  public void setRentalConditions(String rentalConditions);

  public boolean isActive();

  public void setActive(boolean active);
}
