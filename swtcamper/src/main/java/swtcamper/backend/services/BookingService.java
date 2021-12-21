package swtcamper.backend.services;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Service
public class BookingService {

  @Autowired
  private BookingRepository bookingRepository;

  public Booking create(
    User user,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate
  ) {
    return bookingRepository.save(new Booking(user, offer, startDate, endDate));
  }

  public Booking update(
    Long bookingID,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws GenericServiceException {
    // Search for booking in database
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      // Booking found so update can be made
      Booking booking = bookingOptional.get();
      booking.setStartDate(startDate);
      booking.setEndDate(endDate);
      booking.setActive(active);
      // Save update back to database
      return bookingRepository.save(booking);
    }
    throw new GenericServiceException(
      "Booking not found. Update not possible."
    );
  }

  public Booking deactivate(Long bookingID) throws GenericServiceException {
    // Search for booking in database
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      // Booking found so update can be made
      Booking booking = bookingOptional.get();
      // Update by setting active = false
      // Save update back to database
      return this.update(
          booking.getId(),
          booking.getStartDate(),
          booking.getEndDate(),
          false
        );
    }
    throw new GenericServiceException(
      "Booking not found. Deactivation not possible."
    );
  }

  public void delete(Long bookingID) throws GenericServiceException {
    try {
      bookingRepository.deleteById(bookingID);
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }
}
