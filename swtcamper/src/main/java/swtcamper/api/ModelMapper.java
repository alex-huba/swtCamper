package swtcamper.api;

import org.springframework.stereotype.Component;
import swtcamper.api.contract.AvailabilityDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.Availability;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.OfferedObjectType;
import swtcamper.api.contract.OfferedObjectTypeDTO;

/**
 * Maps entities to DTOs.
 */
@Component
public class ModelMapper {

//  public VehicleTypeDTO vehicleTypeToVehicleTypeDTO(VehicleType vehicleType) {
//    // TODO alt throws Exception {
//      switch (vehicleType) {
//        case BUS:
//          return VehicleTypeDTO.BUS;
//        case CAMPER:
//          return VehicleTypeDTO.CAMPER;
//        case TRAILER:
//          return VehicleTypeDTO.TRAILER;
////      TODO alt remove return: null;
////       implement default throws Exception
//        default: return null;
////          throw new GenericServiceException("VehicleType is invalid.");
//      }
//  }
//
  public AvailabilityDTO toAvailabilityDTO(Availability availability) {
    switch (availability) {
      case AVAILABLE:
        return AvailabilityDTO.AVAILABLE;
      case RENT:
        return AvailabilityDTO.RENT;
      case RESERVED:
        return AvailabilityDTO.RESERVED;
//      TODO alt remove return: null;
//       implement default throws Exception
        default: return null;
        // throw new GenericServiceException("Availability value is invalid.");
    }
  }

    public OfferedObjectTypeDTO toOfferedObjectTypeDTO(OfferedObjectType offeredObjectType) {
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
                toAvailabilityDTO(offer.getAvailability()),
                offer.getPrice(),
                offer.getRentalStartDate(),
                offer.getRentalReturnDate()
        );
    }
}
