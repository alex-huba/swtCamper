package swtcamper.backend.services;

import java.awt.print.Book;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.contract.BookingDTO;
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
    LocalDate endDate,
    boolean active
  ) {
    // TODO: implement booking creation
    return null;
  }

  public Booking update(
    Long bookingID,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
  ) throws GenericServiceException {
    // TODO: implement booking update
    return null;
  }

  public Booking deactivate(Long bookingID) throws GenericServiceException {
    // TODO: implement booking creation
    return null;
  }
}
