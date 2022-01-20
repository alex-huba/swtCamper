package swtcamper.api.contract.interfaces;

import java.time.LocalDate;
import java.util.List;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;

public interface IBookingController {
  List<Booking> getAllBookings();
  List<Booking> getBookingsForUser(User user);

  BookingDTO create(
    User user,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate
  ) throws UserDoesNotExistException, GenericServiceException;

  BookingDTO update(
    Long bookingID,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws GenericServiceException;

  /**
   * Updates a booking by setting active = true.
   * @param bookingID
   * @throws GenericServiceException
   */
  BookingDTO activate(Long bookingID) throws GenericServiceException;

  /**
   * Updates a booking by setting active = false.
   * @param bookingID
   * @throws GenericServiceException
   */
  BookingDTO deactivate(Long bookingID) throws GenericServiceException;

  /**
   * Deletes a booking by its ID.
   * @param bookingID
   * @throws GenericServiceException
   */
  void delete(Long bookingID) throws GenericServiceException;

  void reject(long bookingID);

  List<OfferDTO> getAvailableOffers(LocalDate startDate, LocalDate endDate)
    throws GenericServiceException;
}
