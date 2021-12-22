package swtcamper.backend.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Service
public class BookingService {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private OfferRepository offerRepository;

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

  public List getBookedDays(long offerID) {
    List<LocalDate> bookedDays = new ArrayList<>();
    Optional<Offer> offerResponse = offerRepository.findById(offerID);
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

  public ArrayList<Long> getAvailableOffers(
    LocalDate startDate,
    LocalDate endDate
  ) {
    // Liste aller angefragten Tage
    List<LocalDate> requestedDays = new ArrayList<>();
    long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
    for (int i = 0; i <= amountOfDays; i++) {
      requestedDays.add(startDate.plus(i, ChronoUnit.DAYS));
    }

    // Liste die returned wird
    ArrayList<Long> offerIDs = new ArrayList<>();
    // Alle Offers holen und die angefragten Tage gegen die bereits gebuchten Tage gegenchecken
    List<Offer> offerResponse = offerRepository.findAll();
    for (Offer offer : offerResponse) {
      List<LocalDate> bookedDays = getBookedDays(offer.getOfferID());
      for (LocalDate requestedDay : requestedDays) {
        if (!bookedDays.contains(requestedDay)) {
          offerIDs.add(offer.getOfferID());
        }
      }
    }
    return offerIDs;
  }

  public boolean offerStillAvailable(long offerID, long bookingID) {
    Optional<Offer> offerResponse = offerRepository.findById(offerID);
    Offer offer = offerResponse.get();
    Optional<Booking> bookingResponse = bookingRepository.findById(bookingID);
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
}
