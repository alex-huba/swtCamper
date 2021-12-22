package swtcamper.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.IBookingController;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.javafx.controller.OfferViewController;

@Component
public class BookingController implements IBookingController {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private OfferViewController offerViewController;

  @Override
  public BookingDTO create(
    User user,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) {
    return modelMapper.bookingToBookingDTO(
      bookingService.create(user, offer, startDate, endDate)
    );
  }

  @Override
  public List<BookingDTO> bookings() throws GenericServiceException {
    return modelMapper.bookingsToBookingDTOs(bookingService.bookings());
  }

  @Override
  public BookingDTO update(
    Long bookingID,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws GenericServiceException {
    try {
      return modelMapper.bookingToBookingDTO(
        bookingService.update(bookingID, startDate, endDate, active)
      );
    } catch (GenericServiceException e) {
      throw new GenericServiceException(e.getMessage());
    }
  }

  @Override
  public BookingDTO deactivate(Long bookingID) throws GenericServiceException {
    try {
      return modelMapper.bookingToBookingDTO(
        bookingService.deactivate(bookingID)
      );
    } catch (GenericServiceException e) {
      throw new GenericServiceException(e.getMessage());
    }
  }
}
