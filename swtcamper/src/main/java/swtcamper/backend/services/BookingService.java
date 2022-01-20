package swtcamper.backend.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.controller.LoggingController;
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
  private LoggingController loggingController;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * Get a List of all available bookings
   * @return List of all available bookings
   */
  public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }

  /**
   * Get a list of all bookings that were created by a specified User
   * @param user User to look for bookings created by him/her
   * @return list of all bookings that were created by the user
   */
  public List<Booking> getBookingsForUser(User user) {
    return getAllBookings()
      .stream()
      .filter(booking ->
        booking.getOffer().getCreator().getId().equals(user.getId())
      )
      .collect(Collectors.toList());
  }

  /**
   * Creates a new booking (active state will be false by default ({@link Booking}))
   * @param user User that wants to book the given offer
   * @param offer {@link Offer} that the given User wants to book
   * @param startDate date to begin the booking
   * @param endDate date until the booking shall go
   * @return created Booking
   */
  public Booking create(
    User user,
    Offer offer,
    LocalDate startDate,
    LocalDate endDate
  ) throws GenericServiceException {
    long newBookingId = bookingRepository
      .save(new Booking(user, offer, startDate, endDate))
      .getId();
    loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s booked offer with ID %s.",
            user.getUsername(),
            offer.getOfferID()
          )
      )
    );
    // add the bookingID to the offer
    Optional<Offer> offerResponse = offerRepository.findById(
      offer.getOfferID()
    );
    if (offerResponse.isPresent()) {
      Offer tempOffer = offerResponse.get();
      ArrayList<Long> bookings = tempOffer.getBookings();
      bookings.add(newBookingId);
      tempOffer.setBookings(bookings);
      offerRepository.save(tempOffer);
    } else {
      throw new GenericServiceException(
        "Offer for this booking not found. Booking creation not possible."
      );
    }
    return bookingRepository.findById(newBookingId).get();
  }

  /**
   * Updates a booking with new values
   * @param bookingID
   * @param startDate
   * @param endDate
   * @param active
   * @param user
   * @return the updated booking
   * @throws GenericServiceException
   */
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
      if (booking.isActive()) {
        throw new GenericServiceException(
          "The booking with ID " +
          bookingID +
          " cannot be deleted since it is still active."
        );
      }
      booking.setStartDate(startDate);
      booking.setEndDate(endDate);
      booking.setActive(active);

      loggingController.log(
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

  /**
   * Activates a booking (will be active)
   * @param bookingID ID of the booking to activate
   * @param user User that gave the order to activate this booking (for logging)
   * @return activated booking
   * @throws GenericServiceException
   */
  public Booking activate(Long bookingID, UserDTO user)
    throws GenericServiceException {
    // Search for booking in database
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      // Booking found so update can be made
      Booking booking = bookingOptional.get();
      // Update by setting active = true
      booking.setActive(true);

      loggingController.log(
          new LoggingMessage(
            LoggingLevel.INFO,
            String.format(
              "Booking with ID %s was activated by user %s.",
              bookingID,
              user.getUsername()
            )
        )
      );
      // Save update back to database
      return bookingRepository.save(booking);
    }
    throw new GenericServiceException(
      "Booking not found. Activation not possible."
    );
  }

  /**
   * Deactivates a booking (will not be active anymore)
   * @param bookingID ID of the booking to deactivate
   * @param user User that gave the order to deactivate this booking (for logging)
   * @return deactivated booking
   * @throws GenericServiceException
   */
  public Booking deactivate(Long bookingID, UserDTO user)
    throws GenericServiceException {
    // Search for booking in database
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      // Booking found so update can be made
      Booking booking = bookingOptional.get();
      // Update by setting active = false
      booking.setActive(false);

      loggingController.log(
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
      return bookingRepository.save(booking);
    }
    throw new GenericServiceException(
      "Booking not found. Deactivation not possible."
    );
  }

  /**
   * Deletes a booking from the database
   * @param bookingID ID of the booking that shall be deleted
   * @param user user that gave the order to delete this booking (for logging)
   * @throws GenericServiceException
   */
  public void delete(Long bookingID, UserDTO user)
    throws GenericServiceException {
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      // Booking found
      Booking booking = bookingOptional.get();
      // can't delete if booking is active
      if (booking.isActive()) {
        throw new GenericServiceException(
          "The booking with ID " +
          bookingID +
          " cannot be deleted since it is still active."
        );
      }
      try {
        // remove booking to be deleted from bookings list of its offer
        Optional<Offer> offerOptional = offerRepository.findById(
          booking.getOffer().getOfferID()
        );
        if (offerOptional.isPresent()) {
          Offer offer = offerOptional.get();
          ArrayList<Long> bookings = offer.getBookings();
          bookings.remove(bookingID);
          offer.setBookings(bookings);
          offerRepository.save(offer);
          // then delete the booking
          bookingRepository.deleteById(bookingID);
          loggingController.log(
              new LoggingMessage(
                LoggingLevel.INFO,
                String.format(
                  "UBooking with ID %s was deleted by user %s",
                  bookingID,
                  user.getUsername()
                )
              )
          );
        } else {
          throw new GenericServiceException(
            "The offer for this booking could not be found."
          );
        }
      } catch (IllegalArgumentException e) {
        throw new GenericServiceException(
          "The passed ID is not available: " + e
        );
      }
    } else {
      throw new GenericServiceException(
        "Booking not found, deletion not possible"
      );
    }
  }

  /**
   * For a specific offer, this method gathers all days on which the offer is booked (by renters) or blocked (by the offer creator). <br> That means each startDate and endDate and all days in between.
   *    *
   * @param offerID
   * @return a list of the booked days
   * @throws GenericServiceException
   */
  public List<LocalDate> getBookedDays(long offerID)
    throws GenericServiceException {
    List<LocalDate> bookedOrBlockedDays = new ArrayList<>();

    Optional<Offer> offerResponse = offerRepository.findById(offerID);
    if (offerResponse.isPresent()) {
      Offer offer = offerResponse.get();
      ArrayList<Long> bookingIDs = offer.getBookings();
      Iterable<Booking> bookings = bookingRepository.findAllById(bookingIDs);
      ArrayList<Pair> blockedDates = offer.getBlockedDates();

      for (Booking booking : bookings) {
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();
        long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= amountOfDays; i++) {
          bookedOrBlockedDays.add(startDate.plus(i, ChronoUnit.DAYS));
        }
      }
      for (Pair pair : blockedDates) {
        LocalDate startDate = (LocalDate) pair.getKey();
        LocalDate endDate = (LocalDate) pair.getValue();
        long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= amountOfDays; i++) {
          bookedOrBlockedDays.add(startDate.plus(i, ChronoUnit.DAYS));
        }
      }
      return bookedOrBlockedDays;
    }
    throw new GenericServiceException(
      "Offer with following ID not found: " + offerID
    );
  }

  /**
   * This method is used by the search functionality. It takes a startDate and an endDate and gathers all offers which are not booked on and in between these days, i.e. are available for the requested period.
   *
   * @param startDate
   * @param endDate
   * @return a list of offerIDs of the available offers
   * @throws GenericServiceException
   */
  public List<Offer> getAvailableOffers(LocalDate startDate, LocalDate endDate)
    throws GenericServiceException {
    if (startDate == null || endDate == null) {
      return offerRepository.findAll();
    }

    // List of all requested days
    List<LocalDate> requestedDays = new ArrayList<>();
    long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
    for (int i = 0; i <= amountOfDays; i++) {
      requestedDays.add(startDate.plus(i, ChronoUnit.DAYS));
    }

    // List that is going to be returned
    List<Offer> offers = new ArrayList<>();

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
          offers.add(offer);
        }
      } catch (GenericServiceException e) {
        throw new GenericServiceException(e.getMessage());
      }
    }
    return offers;
  }

  /**
   * Checks if a booking being re-activated is still available (in case the booked period was booked by someone else while the booking was deactivated).
   *
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

  public void reject(long bookingID) {
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingID);
    if (bookingOptional.isPresent()) {
      Booking booking = bookingOptional.get();
      booking.setRejected(true);
      bookingRepository.save(booking);
    }
  }
}
