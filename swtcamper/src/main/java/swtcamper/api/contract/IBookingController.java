package swtcamper.api.contract;

import java.time.LocalDate;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;

public interface IBookingController {
  public BookingDTO create(
    User user,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws UserDoesNotExistException, GenericServiceException;

  public BookingDTO update(
    Long bookingID,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws GenericServiceException;

  /**
   * Updates an offer by setting active = false.
   *
   * @param bookingID
   * @throws GenericServiceException
   */
  public BookingDTO deactivate(Long bookingID) throws GenericServiceException;
}
