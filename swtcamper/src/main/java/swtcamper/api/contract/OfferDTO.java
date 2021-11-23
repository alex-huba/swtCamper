package xtasks.api.contract;

import xtasks.backend.entities.Availability;
import xtasks.backend.entities.OfferedObjectType;
import xtasks.backend.entities.OfferedObjectTypeDTO;

public class OfferDTO {
    private Long offerID;

    private OfferedObjectTypeDTO offeredObjectType;
    Long offeredObjectID;

    private AvailabilityDTO availability;

    private Long price;

    private String rentalStartDate;
    private String rentalReturnDate;
}
