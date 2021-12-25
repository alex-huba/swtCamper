package swtcamper.backend.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Service
public class BookingService {

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private LoggingService loggingService;

  public Booking create(
    User user,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate
  ) {
    long newBookingId = bookingRepository
      .save(new Booking(user, offer, startDate, endDate))
      .getId();
    loggingService.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "User %s booked offer with ID %s.",
          user.getUsername(),
          offer.getOfferID()
        )
      )
    );
    return bookingRepository.findById(newBookingId).get();
  }

  public Booking update(
    Long bookingID,
    LocalDate startDate,
    LocalDate endDate,
    boolean active,
    UserDTO user
  ) throws GenericServiceException {
    // Search for booking in database
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      // Booking found so update can be made
      Booking booking = bookingOptional.get();
      booking.setStartDate(startDate);
      booking.setEndDate(endDate);
      booking.setActive(active);

      loggingService.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "Booking with ID %s got updated by user %s.",
            bookingID,
            user.getUsername()
          )
        )
      );
      // Save update back to database
      return bookingRepository.save(booking);
    }
    throw new GenericServiceException(
      "Booking not found. Update not possible."
    );
  }

  public Booking deactivate(Long bookingID, UserDTO user)
    throws GenericServiceException {
    // Search for booking in database
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      // Booking found so update can be made
      Booking booking = bookingOptional.get();
      // Update by setting active = false
      loggingService.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "Booking with ID %s was deactivated by user %s.",
            bookingID,
            user.getUsername()
          )
        )
      );
      // Save update back to database
      return this.update(
          booking.getId(),
          booking.getStartDate(),
          booking.getEndDate(),
          false,
          user
        );
    }
    throw new GenericServiceException(
      "Booking not found. Deactivation not possible."
    );
  }

  public void delete(Long bookingID, UserDTO user)
    throws GenericServiceException {
    try {
      bookingRepository.deleteById(bookingID);
      loggingService.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "UBooking with ID %s was deleted by user %s",
            bookingID,
            user.getUsername()
          )
        )
      );
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }

  /**
   * For a specific offer, this method gathers all days on which the offer is booked. <br> That means each startDate and endDate and all days in between.
   *
   * @param offerID
   * @return a list of the booked days
   * @throws GenericServiceException
   */
  public List<LocalDate> getBookedDays(long offerID)
    throws GenericServiceException {
    List<LocalDate> bookedDays = new ArrayList<>();

    Optional<Offer> offerResponse = offerRepository.findById(offerID);
    if (offerResponse.isPresent()) {
      Offer offer = offerResponse.get();
      ArrayList<Long> bookingIDs = offer.getBookings();
      Iterable<Booking> bookings = bookingRepository.findAllById(bookingIDs);

      for (Booking booking : bookings) {
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();
        long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= amountOfDays; i++) {
          bookedDays.add(startDate.plus(i, ChronoUnit.DAYS));
        }
      }
      return bookedDays;
    }
    throw new GenericServiceException(
      "Offer with following ID not found: " + offerID
    );
  }

  /**
   * This method is used by the search functionality. It takes a startDate and an endDate and gathers all offers which are not booked on and in between these days, i.e. are available for the requested period.
   * @param startDate
   * @param endDate
   * @return a list of offerIDs of the available offers
   * @throws GenericServiceException
   */
  public ArrayList<Long> getAvailableOffers(
    LocalDate startDate,
    LocalDate endDate
  ) throws GenericServiceException {
    // List of all requested days
    List<LocalDate> requestedDays = new ArrayList<>();
    long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
    for (int i = 0; i <= amountOfDays; i++) {
      requestedDays.add(startDate.plus(i, ChronoUnit.DAYS));
    }

    // List that is going to be returned
    ArrayList<Long> offerIDs = new ArrayList<>();

    // Get all offers and check requested days against days that are already booked
    List<Offer> offerResponse = offerRepository.findAll();
    for (Offer offer : offerResponse) {
      try {
        List<LocalDate> bookedDays = getBookedDays(offer.getOfferID());
        boolean offerAvailable = true;
        for (LocalDate requestedDay : requestedDays) {
          if (bookedDays.contains(requestedDay)) {
            offerAvailable = false;
          }
        }
        if (offerAvailable) {
          offerIDs.add(offer.getOfferID());
        }
      } catch (GenericServiceException e) {
        throw new GenericServiceException(e.getMessage());
      }
    }
    return offerIDs;
  }

  /**
   * Checks if a booking being re-activated is still available (in case the booked period was booked by someone else while the booking was deactivated).
   * @param offerID
   * @param bookingID
   * @return <b>True</b>, if the offer is still available for the initially booked period (i.e. the booking can be reactivated). <br>
   * <b>False</b>, if the offer is not available anymore for the initially booked period (i.e. the renter has to pick new start and end dates).
   * @throws GenericServiceException
   */
  public boolean offerStillAvailable(long offerID, long bookingID)
    throws GenericServiceException {
    Optional<Booking> bookingResponse = bookingRepository.findById(bookingID);
    if (bookingResponse.isPresent()) {
      Booking booking = bookingResponse.get();

      LocalDate startDate = booking.getStartDate();
      LocalDate endDate = booking.getEndDate();

      List<LocalDate> requestedDays = new ArrayList<>();
      long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
      for (int i = 0; i <= amountOfDays; i++) {
        requestedDays.add(startDate.plus(i, ChronoUnit.DAYS));
      }

      List<LocalDate> bookedDays = getBookedDays(offerID);
      for (LocalDate requestedDay : requestedDays) {
        if (bookedDays.contains(requestedDay)) {
          return false;
        }
      }
      return true;
    }
    throw new GenericServiceException(
      "Booking with following ID not found: " + bookingID
    );
  }
}
