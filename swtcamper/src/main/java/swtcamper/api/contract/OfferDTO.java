package swtcamper.api.contract;

public class OfferDTO {
    private Long offerID;

    private OfferedObjectTypeDTO offeredObjectType;
    Long offeredObjectID;

    private AvailabilityDTO availability;

    private Long price;

    private String rentalStartDate;
    private String rentalReturnDate;

    public OfferDTO(Long offerID, OfferedObjectTypeDTO offeredObjectType, Long offeredObjectID, AvailabilityDTO availability, Long price, String rentalStartDate, String rentalReturnDate) {
        this.offerID = offerID;
        this.offeredObjectType = offeredObjectType;
        this.offeredObjectID = offeredObjectID;
        this.availability = availability;
        this.price = price;
        this.rentalStartDate = rentalStartDate;
        this.rentalReturnDate = rentalReturnDate;
    }
}