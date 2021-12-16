package swtcamper.api.controller;

import java.time.LocalDate;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.IBookingController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;

public class BookingController implements IBookingController {

  @Override
  public BookingDTO create(
    User user,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws UserDoesNotExistException, GenericServiceException {
    // TODO: implement booking creation
    return null;
  }

  @Override
  public BookingDTO update(
    Long bookingID,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws GenericServiceException {
    // TODO: implement booking update
    return null;
  }

  @Override
  public BookingDTO deactivate(Long bookingID) throws GenericServiceException {
    // TODO: implement booking creation
    return null;
  }
}
