package swtcamper.api.contract;

import java.util.ArrayList;

public class OfferDTO {

  private Long offerID;
  private OfferedObjectTypeDTO offeredObjectType;
  private Long offeredObjectID;
  private ArrayList<Long> bookings;
  private Long price;
  private String rentalConditions;
  private boolean active;

  public OfferDTO(
    Long offerID,
    OfferedObjectTypeDTO offeredObjectType,
    Long offeredObjectID,
    ArrayList<Long> bookings,
    Long price,
    String rentalConditions,
    boolean active
  ) {
    this.offerID = offerID;
    this.offeredObjectType = offeredObjectType;
    this.offeredObjectID = offeredObjectID;
    this.bookings = bookings;
    this.price = price;
    this.rentalConditions = rentalConditions;
    this.active = active;
  }
}
