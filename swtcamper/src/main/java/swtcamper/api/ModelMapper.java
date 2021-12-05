package swtcamper.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.OfferedObjectTypeDTO;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.OfferedObjectType;
import swtcamper.backend.services.exceptions.GenericServiceException;

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
      offer.getOfferedObject(),
      offer.getBookings(),
      offer.getPrice(),
      offer.isMinAge25(),
      offer.isBorderCrossingAllowed(),
      offer.isDepositInCash(),
      offer.isActive()
    );
  }

  public List<OfferDTO> offersToOfferDTOs(List<Offer> offers)
    throws GenericServiceException {
    List<OfferDTO> offerDTOs = new ArrayList<>();
    for (Offer offer : offers) {
      offerDTOs.add(offerToOfferDTO(offer));
    }
    return offerDTOs;
  }
}
