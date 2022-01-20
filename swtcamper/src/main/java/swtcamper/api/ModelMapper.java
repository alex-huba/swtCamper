package swtcamper.api;

import java.util.ArrayList;
import java.util.List;
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

  public OfferedObjectType offeredObjectTypeDTOToOfferedObjectType(
    OfferedObjectTypeDTO offeredObjectTypeDTO
  ) {
    switch (offeredObjectTypeDTO) {
      case VEHICLE:
        return OfferedObjectType.VEHICLE;
      default:
        return null;
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
      offer.getRentalConditions(),
      offer.getBlockedDates(),
      offer.isActive(),
      offer.isPromoted()
    );
  }

  public Offer offerDTOToOffer(OfferDTO offerDTO) {
    Offer offer = new Offer();
    offer.setOfferID(offerDTO.getID());
    offer.setCreator(offerDTO.getCreator());
    offer.setOfferedObjectType(
      offeredObjectTypeDTOToOfferedObjectType(offerDTO.getOfferedObjectType())
    );
    offer.setOfferedObject(offerDTO.getOfferedObject());
    offer.setBookings(offerDTO.getBookings());
    offer.setTitle(offerDTO.getTitle());
    offer.setLocation(offerDTO.getLocation());
    offer.setContact(offerDTO.getContact());
    offer.setParticularities(offerDTO.getParticularities());
    offer.setPrice(offerDTO.getPrice());
    offer.setRentalConditions(offerDTO.getRentalConditions());
    offer.setBlockedDates(offerDTO.getBlockedDates());
    offer.setActive(offerDTO.isActive());
    offer.setPromoted(offerDTO.isPromoted());

    return offer;
  }

  public List<OfferDTO> offersToOfferDTOs(List<Offer> offers)
    throws GenericServiceException {
    List<OfferDTO> offerDTOs = new ArrayList<>();
    for (Offer offer : offers) {
      offerDTOs.add(offerToOfferDTO(offer));
    }
    return offerDTOs;
  }

  public List<Offer> offerDTOsToOffer(List<OfferDTO> offerDTOList) {
    List<Offer> offerList = new ArrayList<>();
    for (OfferDTO offerDTO : offerDTOList) {
      offerList.add(offerDTOToOffer(offerDTO));
    }
    return offerList;
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
      booking.getRenter(),
      booking.getOffer(),
      booking.getStartDate(),
      booking.getEndDate(),
      booking.isActive()
    );
  }

  public PictureDTO pictureToPictureDTO(Picture picture) {
    return new PictureDTO(
      picture.getPictureID(),
      picture.getVehicleID(),
      picture.getPath()
    );
  }

  public Picture pictureDTOToPicture(PictureDTO pictureDTO) {
    return new Picture(pictureDTO);
  }

  public List<PictureDTO> picturesToPictureDTOs(List<Picture> pictures) {
    List<PictureDTO> pictureDTOs = new ArrayList<>();
    for (Picture picture : pictures) {
      pictureDTOs.add(pictureToPictureDTO(picture));
    }
    return pictureDTOs;
  }

  public LoggingMessageDTO loggingMessageToLoggingMessageDTO(
    LoggingMessage loggingMessage
  ) {
    return new LoggingMessageDTO(
      loggingMessage.getLoggingMessageID(),
      loggingMessage.getTime(),
      loggingMessage.getLogLevel(),
      loggingMessage.getLoggingMessage()
    );
  }

  public LoggingMessage loggingMessageDTOToLoggingMessage(
    LoggingMessageDTO loggingMessageDTO
  ) {
    LoggingMessage loggingMessage = new LoggingMessage();
    loggingMessage.setLoggingMessageID(loggingMessageDTO.getLoggingMessageID());
    loggingMessage.setTime(loggingMessageDTO.getTime());
    loggingMessage.setLogLevel(loggingMessageDTO.getLogLevel());
    loggingMessage.setLoggingMessage(loggingMessageDTO.getLoggingMessage());

    return loggingMessage;
  }

  public List<LoggingMessageDTO> LoggingMessagesToLoggingMessageDTOs(
    List<LoggingMessage> loggingMessageList
  ) {
    List<LoggingMessageDTO> loggingMessageDTOList = new ArrayList<>();
    for (LoggingMessage loggingMessage : loggingMessageList) {
      loggingMessageDTOList.add(
        loggingMessageToLoggingMessageDTO(loggingMessage)
      );
    }

    return loggingMessageDTOList;
  }

  public UserReportDTO userReportToUserReportDTO(UserReport userReport) {
    return new UserReportDTO(userReport.getId(),userReport.isActive(),userReport.getReporter(),userReport.getReportee(),userReport.getReportReason());
  }

  public List<UserReportDTO> userReportsToUserReportDTOs(List<UserReport> userReportList) {
    List<UserReportDTO> userReportDTOList = new ArrayList<>();
    for(UserReport userReport : userReportList) {
      userReportDTOList.add(userReportToUserReportDTO(userReport));
    }
    return userReportDTOList;
  }
}
