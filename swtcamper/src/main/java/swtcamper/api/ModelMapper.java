package swtcamper.api;

import org.springframework.stereotype.Component;
import swtcamper.api.contract.*;
import swtcamper.backend.entities.*;
import swtcamper.backend.services.exceptions.GenericServiceException;

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
      default:
        return null;
      // throw new GenericServiceException("Availability value is invalid.");
    }
  }

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
      toAvailabilityDTO(offer.getAvailability()),
      offer.getPrice(),
      offer.getRentalStartDate(),
      offer.getRentalReturnDate()
    );
  }

  public UserDTO userToUserDTO(User user){
    return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getPhone(),
            user.getPassword(),
            user.getUserRole(),
            user.isLocked(),
            user.isEnabled()
    );
  }

  public UserRoleDTO toUserRoleDTO(userRole userRole)
          throws GenericServiceException {
    switch (userRole){
      case USER:
        return UserRoleDTO.USER;
      case OPERATOR:
        return UserRoleDTO.OPERATOR;
      default:
        throw new GenericServiceException("UserRole is invalid.");
    }
  }
}
