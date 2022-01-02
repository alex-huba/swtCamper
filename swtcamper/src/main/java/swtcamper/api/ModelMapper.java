package swtcamper.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.*;
import swtcamper.backend.entities.*;
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
      offer.getCreator(),
      toOfferedObjectTypeDTO(offer.getOfferedObjectType()),
      offer.getOfferedObject(),
      offer.getBookings(),
      offer.getTitle(),
      offer.getLocation(),
      offer.getContact(),
      offer.getParticularities(),
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

  public UserDTO userToUserDTO(User user) {
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

  public UserRoleDTO toUserRoleDTO(UserRole userRole)
    throws GenericServiceException {
    switch (userRole) {
      case PROVIDER:
        return UserRoleDTO.PROVIDER;
      case RENTER:
        return UserRoleDTO.RENTER;
      case OPERATOR:
        return UserRoleDTO.OPERATOR;
      default:
        throw new GenericServiceException("UserRole is invalid.");
    }
  }

  public BookingDTO bookingToBookingDTO(Booking booking) {
    return new BookingDTO(
      booking.getId(),
      booking.getUser(),
      booking.getOffer(),
      booking.getStartDate(),
      booking.getEndDate(),
      booking.isActive()
    );
  }
}
