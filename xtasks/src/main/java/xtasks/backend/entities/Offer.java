package xtasks.backend.entities;

public interface Offer {
  //    enum definieren --> type = Type.VEHICLE;

  //    public OfferedObject getOfferedObject();
  //
  //    public void setOfferedObject(OfferedObject offeredObject);

  public Availability getAvailability();

  public void setAvailability(Availability availability);

  public Long getPrice();

  public void setPrice(Long price);

  public String getRentalStartDate();

  public void setRentalStartDate(String rentalStartDate);

  public String getRentalReturnDate();

  public void setRentalReturnDate(String rentalReturnDate);
}
