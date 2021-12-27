package swtcamper.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.IBookingController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class BookingController implements IBookingController {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private ModelMapper modelMapper;

  public List<Booking> getAllBookings() {
    return bookingService.getAllBookings();
  }

  public List<Booking> getBookingsForUser(User user) {
    return getAllBookings().stream().filter(booking -> booking.getOffer().getCreator().getId().equals(user.getId())).collect(Collectors.toList());
  }

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

  public void delete(Long bookingID) throws GenericServiceException {
    bookingService.delete(bookingID);
  }
}
