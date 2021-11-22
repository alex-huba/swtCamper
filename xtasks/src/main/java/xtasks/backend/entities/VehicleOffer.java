package xtasks.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VehicleOffer implements Offer {

  @Id
  @GeneratedValue
  private Long offerID;

  private Availability availability;

  Long vehicleID;

  private Long price;

  private String rentalStartDate;
  private String rentalReturnDate;

  // hier lieber direkt vehicleID übergeben statt Vehicle selbst?
  // Hab es erstmal so gelassen, damit das refactoring bei den anderen nicht zu kompliziert wird
  public VehicleOffer(Vehicle vehicle, Long price) {
    this.vehicleID = vehicle.getVehicleID();
    this.price = price;
    this.availability = Availability.AVAILABLE;
  }

  public VehicleOffer(Vehicle vehicle) {
    this.vehicleID = vehicle.getVehicleID();
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

  //  @Override
  //  public OfferedObject getOfferedObject() {
  //    return null;
  //  }
  //
  //  @Override
  //  public void setOfferedObject(OfferedObject offeredObject) {
  //
  //  }

  public Long getVehicleID() {
    return vehicleID;
  }

  public void setVehicle(Long vehicleID) {
    this.vehicleID = vehicleID;
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
