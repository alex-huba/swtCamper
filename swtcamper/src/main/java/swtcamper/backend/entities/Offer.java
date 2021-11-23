package swtcamper.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Offer implements IOffer {

    @Id
    @GeneratedValue
    private Long offerID;

    private OfferedObjectType offeredObjectType;
    Long offeredObjectID;

    private Availability availability;

    private Long price;

    private String rentalStartDate;
    private String rentalReturnDate;


    public Offer(Vehicle vehicle, Long price) {
        this.offeredObjectType = OfferedObjectType.VEHICLE;
        this.offeredObjectID = vehicle.getVehicleID();
        this.price = price;
        this.availability = Availability.AVAILABLE;
    }

    public Offer(Vehicle vehicle) {
        this.offeredObjectID = vehicle.getVehicleID();
        this.offeredObjectType = OfferedObjectType.VEHICLE;
    }

    public Offer() {
    }

    public Long getOfferID() {
        return offerID;
    }

    public void setOfferID(Long offerID) {
        this.offerID = offerID;
    }

    public OfferedObjectType getOfferedObjectType() {
        return offeredObjectType;
    }

    public void setOfferedObjectType(OfferedObjectType offeredObjectType) {
        this.offeredObjectType = offeredObjectType;
    }

    public Long getOfferedObjectID() {
        return offeredObjectID;
    }

    public void setOfferedObjectID(Long vehicleID) {
        this.offeredObjectID = vehicleID;
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
