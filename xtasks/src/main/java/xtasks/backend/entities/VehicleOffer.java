package xtasks.backend.entities;

public class VehicleOffer implements Offer {

  Vehicle vehicle;
  Availability availability;

  Long price;

  String rentalStartDate;
  String rentalReturnDate;

  public VehicleOffer(Vehicle vehicle, Long price) {
    this.vehicle = vehicle;
    this.price = price;
    this.availability = Availability.AVAILABLE;
  }

  public VehicleOffer(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  public VehicleOffer() {}

  //    @Override
  //    public Vehicle getOfferedObject() {
  //        return vehicle;
  //    }
  //
  //    @Override
  //    public void setOfferedObject(T offeredObject) {
  //
  //    }

  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  @Override
  public Availability getAvailability() {
    return availability;
  }

  @Override
  public void setAvailability(Availability availability) {
    this.availability = availability;
  }

  @Override
  public Long getPrice() {
    return price;
  }

  @Override
  public void setPrice(Long price) {
    this.price = price;
  }

  @Override
  public String getRentalStartDate() {
    return rentalStartDate;
  }

  @Override
  public void setRentalStartDate(String rentalStartDate) {
    this.rentalStartDate = rentalStartDate;
  }

  @Override
  public String getRentalReturnDate() {
    return rentalReturnDate;
  }

  @Override
  public void setRentalReturnDate(String rentalReturnDate) {
    this.rentalReturnDate = rentalReturnDate;
  }
}
