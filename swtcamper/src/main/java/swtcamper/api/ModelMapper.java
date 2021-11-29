package swtcamper.api;

import org.springframework.stereotype.Component;
import swtcamper.api.contract.AvailabilityDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.OfferedObjectTypeDTO;
import swtcamper.backend.entities.Availability;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.OfferedObjectType;

/**
 * Maps entities to DTOs.
 */
@Component
public class ModelMapper {

  public OfferedObjectTypeDTO toOfferedObjectTypeDTO(
    OfferedObjectType offeredObjectType
  ) {
    switch (offeredObjectType) {
      case VEHICLE:
        return OfferedObjectTypeDTO.VEHICLE;
      default:
        return null;
      // TODO alt remove return: null
      //  implement default throws Exception
      //  throw new GenericServiceException("Availability value is invalid.");
    }
  }

  public OfferDTO offerToOfferDTO(Offer offer) {
    return new OfferDTO(
      offer.getOfferID(),
            toOfferedObjectTypeDTO(offer.getOfferedObjectType()),
            offer.getOfferedObjectID(),
            offer.getBookings(),
            offer.getPrice(),
            offer.getRentalConditions(),
            offer.isActive()
    );
  }
}
