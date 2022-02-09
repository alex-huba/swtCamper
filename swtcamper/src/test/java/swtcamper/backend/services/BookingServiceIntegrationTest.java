package swtcamper.backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.util.Pair;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceIntegrationTest {

  @Autowired
  private BookingService bookingServiceUnderTest;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ModelMapper modelMapper;

  // ------------- Setup & helper methods -------------

  @BeforeEach
  public void clearRepositories() {
    bookingRepository.deleteAll();
    offerRepository.deleteAll();
    vehicleRepository.deleteAll();
    userRepository.deleteAll();
  }

  public User createUniqueUser() {
    double rand = Math.random();
    User user = new User();
    user.setUsername(String.valueOf(rand));
    user.setEmail(String.valueOf(rand));
    user.setPhone(String.valueOf(rand));
    return userRepository.save(user);
  }

  public Offer createUniqueOffer() {
    User user = createUniqueUser();
    Vehicle vehicle = new Vehicle();
    vehicle = vehicleRepository.save(vehicle);
    Offer offer = new Offer(user, vehicle);
    ArrayList<Pair> blockedDates = new ArrayList<>();
    offer.setBlockedDates(blockedDates);
    return offerRepository.save(offer);
  }

  public void fillRepoWithBookings(int x) {
    for (long i = 1; i <= x; i++) {
      User user = new User();
      user.setUsername(String.valueOf(i));
      user.setEmail(String.valueOf(i));
      user.setPhone(String.valueOf(i));
      user = userRepository.save(user);
      Vehicle vehicle = new Vehicle();
      vehicle = vehicleRepository.save(vehicle);
      Offer offer = new Offer(user, vehicle);
      offer = offerRepository.save(offer);
      for (int j = 0; j < i; j++) {
        Booking booking = new Booking(
          user,
          offer,
          LocalDate.now(),
          LocalDate.now().plus(1, ChronoUnit.DAYS)
        );
        bookingRepository.save(booking);
      }
    }
  }

  // ------------- TESTING -------------

  @Test
  public void getAllBookingsShouldReturnAllBookings() {
    // arrange
    Booking booking = new Booking();
    List<Booking> expected = new ArrayList<>();
    expected.add(booking);
    bookingRepository.saveAll(expected);
    // act
    List<Booking> actual = bookingServiceUnderTest.getAllBookings();
    // assert
    Booking actualBooking = actual.get(0);
    Booking expectedBooking = expected.get(0);
    assertThat(actualBooking)
      .usingRecursiveComparison()
      .isEqualTo(expectedBooking);
  }

  @Test
  public void getBookingsForUserShouldReturnOnlyAndAllBookingsOfUser() {
    // arrange
    fillRepoWithBookings(3);
    User user = new User();
    user.setId(2L);
    // act
    List<Booking> actual = bookingServiceUnderTest.getBookingsForUser(user);
    // assert
    for (Booking booking : actual) {
      assertThat(booking.getOffer().getCreator().getId()).isEqualTo(2L);
    }
  }

  @Test
  public void getBookingsForUserShouldReturnEmptyListIfUsersOffersHaveNoBookings() {
    // arrange
    fillRepoWithBookings(3);
    User user = new User();
    user.setId(4L);
    // act
    List<Booking> actual = bookingServiceUnderTest.getBookingsForUser(user);
    // assert
    assertTrue(actual.isEmpty());
  }

  @Test
  public void createShouldPersistAndReturnNewBooking()
    throws GenericServiceException {
    // arrange
    User user = createUniqueUser();
    Offer offer = createUniqueOffer();
    Booking expected = new Booking(
      user,
      offer,
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS)
    );
    // act
    Booking actual = bookingServiceUnderTest.create(
      user,
      offer,
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS)
    );
    // assert
    assertThat(actual)
      .usingRecursiveComparison()
      .ignoringFields("id", "offer")
      .isEqualTo(expected);
    assertEquals(offer.getOfferID(), actual.getOffer().getOfferID());
  }

  @Test
  public void updateShouldUpdateBooking() throws GenericServiceException {
    // arrange
    Booking booking = bookingRepository.save(
      new Booking(
        createUniqueUser(),
        createUniqueOffer(),
        LocalDate.now(),
        LocalDate.now().plus(1, ChronoUnit.DAYS)
      )
    );
    // act
    Booking actual = bookingServiceUnderTest.update(
      booking.getId(),
      LocalDate.now().plus(3, ChronoUnit.DAYS),
      LocalDate.now().plus(4, ChronoUnit.DAYS),
      true,
      modelMapper.userToUserDTO(booking.getRenter())
    );
    // assert
    assertEquals(
      actual.getStartDate(),
      LocalDate.now().plus(3, ChronoUnit.DAYS)
    );
    assertEquals(actual.getEndDate(), LocalDate.now().plus(4, ChronoUnit.DAYS));
    assertTrue(actual.isActive());
  }

  @Test
  public void updateShouldThrowGSEIfBookingNotFound() {
    // act & assert
    Exception exception = assertThrows(
      GenericServiceException.class,
      () -> {
        bookingServiceUnderTest.update(
          1L,
          LocalDate.now(),
          LocalDate.now().plus(1, ChronoUnit.DAYS),
          false,
          new UserDTO()
        );
      }
    );
    String expected = "Booking not found. Update not possible.";
    String actual = exception.getMessage();
    assertEquals(expected, actual);
  }

  @Test
  public void updateShouldThrowGSEIfBookingIsActive() {
    // arrange
    Booking booking = new Booking(
      createUniqueUser(),
      createUniqueOffer(),
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS)
    );
    booking.setActive(true);
    bookingRepository.save(booking);
    // act & assert
    Exception exception = assertThrows(
      GenericServiceException.class,
      () -> {
        bookingServiceUnderTest.update(
          booking.getId(),
          booking.getStartDate(),
          booking.getEndDate(),
          booking.isActive(),
          modelMapper.userToUserDTO(booking.getRenter())
        );
      }
    );
    String expected =
      "The booking with ID " +
      booking.getId() +
      " cannot be updated since it is still active.";
    String actual = exception.getMessage();
    assertEquals(expected, actual);
  }

  @Test
  public void activateShouldSetActiveToTrue() throws GenericServiceException {
    // arrange
    Booking booking = bookingRepository.save(
      new Booking(
        createUniqueUser(),
        createUniqueOffer(),
        LocalDate.now(),
        LocalDate.now().plus(1, ChronoUnit.DAYS)
      )
    );
    // act
    Booking actual = bookingServiceUnderTest.activate(
      booking.getId(),
      modelMapper.userToUserDTO(booking.getRenter())
    );
    // assert
    assertTrue(actual.isActive());
  }

  @Test
  public void activateShouldThrowGSEIfBookingNotFound() {
    // act & assert
    Exception exception = assertThrows(
      GenericServiceException.class,
      () -> {
        bookingServiceUnderTest.activate(1L, new UserDTO());
      }
    );
    String expected = "Booking not found. Activation not possible.";
    String actual = exception.getMessage();
    assertEquals(expected, actual);
  }

  @Test
  public void deactivateShouldSetActiveToFalse()
    throws GenericServiceException {
    // arrange
    Booking booking = new Booking(
      createUniqueUser(),
      createUniqueOffer(),
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS)
    );
    booking.setActive(true);
    bookingRepository.save(booking);
    // act
    Booking actual = bookingServiceUnderTest.deactivate(
      booking.getId(),
      modelMapper.userToUserDTO(booking.getRenter())
    );
    // assert
    assertFalse(actual.isActive());
  }

  @Test
  public void deactivateShouldThrowGSEIfBookingNotFound() {
    // act & assert
    Exception exception = assertThrows(
      GenericServiceException.class,
      () -> {
        bookingServiceUnderTest.deactivate(1L, new UserDTO());
      }
    );
    String expected = "Booking not found. Deactivation not possible.";
    String actual = exception.getMessage();
    assertEquals(expected, actual);
  }

  @Test
  public void deleteShouldRemoveBookingFromRepository()
    throws GenericServiceException {
    // arrange
    Booking booking = bookingRepository.save(
      new Booking(
        createUniqueUser(),
        createUniqueOffer(),
        LocalDate.now(),
        LocalDate.now().plus(1, ChronoUnit.DAYS)
      )
    );
    // act
    bookingServiceUnderTest.delete(
      booking.getId(),
      modelMapper.userToUserDTO(booking.getRenter())
    );
    // assert
    assertFalse(bookingRepository.findById(booking.getId()).isPresent());
  }

  @Test
  public void deleteShouldThrowGSEIfBookingIsActive() {
    // arrange
    Booking booking = new Booking(
      createUniqueUser(),
      createUniqueOffer(),
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS)
    );
    booking.setActive(true);
    bookingRepository.save(booking);
    // act & assert
    Exception exception = assertThrows(
      GenericServiceException.class,
      () -> {
        bookingServiceUnderTest.delete(
          booking.getId(),
          modelMapper.userToUserDTO(booking.getRenter())
        );
      }
    );
    String expected =
      "The booking with ID " +
      booking.getId() +
      " cannot be deleted since it is still active.";
    String actual = exception.getMessage();
    assertEquals(expected, actual);
  }

  @Test
  public void deleteShouldThrowGSEIfBookingNotFound() {
    // act & assert
    Exception exception = assertThrows(
      GenericServiceException.class,
      () -> {
        bookingServiceUnderTest.delete(1L, new UserDTO());
      }
    );
    String expected = "Booking not found, deletion not possible.";
    String actual = exception.getMessage();
    assertEquals(expected, actual);
  }

  @Test
  public void deleteShouldRemoveBookingFromOffer()
    throws GenericServiceException {
    // arrange
    Offer offer = createUniqueOffer();
    Booking booking = bookingRepository.save(
      new Booking(
        createUniqueUser(),
        offer,
        LocalDate.now(),
        LocalDate.now().plus(1, ChronoUnit.DAYS)
      )
    );
    // act
    bookingServiceUnderTest.delete(
      booking.getId(),
      modelMapper.userToUserDTO(booking.getRenter())
    );
    // assert
    Offer offerWithoutBooking = offerRepository
      .findById(offer.getOfferID())
      .get();
    assertFalse(offerWithoutBooking.getBookings().contains(booking.getId()));
  }

  @Test
  public void getBookedAndBlockedDaysShouldReturnAllBookedAndBlockedDays()
    throws GenericServiceException {
    // arrange
    Offer offer = createUniqueOffer();
    ArrayList<Pair> blockedDates = new ArrayList<>();
    Pair pair = new Pair(
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS)
    );
    blockedDates.add(pair);
    offer.setBlockedDates(blockedDates);
    Booking booking = bookingRepository.save(
      new Booking(
        createUniqueUser(),
        offer,
        LocalDate.now().plus(2, ChronoUnit.DAYS),
        LocalDate.now().plus(3, ChronoUnit.DAYS)
      )
    );
    offer.getBookings().add(booking.getId());
    Offer modifiedOffer = offerRepository.save(offer);
    List<LocalDate> expected = new ArrayList<>();
    Collections.addAll(
      expected,
      LocalDate.now().plus(2, ChronoUnit.DAYS),
      LocalDate.now().plus(3, ChronoUnit.DAYS),
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS)
    );
    // act
    List<LocalDate> actual = bookingServiceUnderTest.getBookedAndBlockedDays(
      modifiedOffer.getOfferID()
    );
    // assert
    assertEquals(expected, actual);
  }

  @Test
  public void getBookedAndBlockedDaysShouldThrowGSEIfOfferNotFound() {
    // act & assert
    Exception exception = assertThrows(
      GenericServiceException.class,
      () -> {
        bookingServiceUnderTest.getBookedAndBlockedDays(1L);
      }
    );
    String expected = "Offer with following ID not found: " + 1L;
    String actual = exception.getMessage();
    assertEquals(expected, actual);
  }

  @Test
  public void getAvailableOffersShouldReturnAllOffersIfArgumentsAreNull()
    throws GenericServiceException {
    // arrange
    Offer offer = createUniqueOffer();
    Long expected = offer.getOfferID();
    // act
    List<Offer> availableOffers = bookingServiceUnderTest.getAvailableOffers(
      null,
      null
    );
    Long actual = availableOffers.get(0).getOfferID();
    // assert
    assertEquals(expected, actual);
  }

  @Test
  public void getAvailableOffersShouldReturnAllAvailableOffers()
    throws GenericServiceException {
    // arrange
    Offer availableOffer = createUniqueOffer();
    Offer nonAvailableOffer = createUniqueOffer();
    Booking booking = bookingRepository.save(
      new Booking(
        createUniqueUser(),
        nonAvailableOffer,
        LocalDate.now(),
        LocalDate.now().plus(1, ChronoUnit.DAYS)
      )
    );
    ArrayList<Long> bookings = new ArrayList<>();
    bookings.add(booking.getId());
    nonAvailableOffer.setBookings(bookings);
    ArrayList<Pair> blockedDates = new ArrayList<>();
    Pair pair = new Pair(
      LocalDate.now().plus(2, ChronoUnit.DAYS),
      LocalDate.now().plus(3, ChronoUnit.DAYS)
    );
    blockedDates.add(pair);
    nonAvailableOffer.setBlockedDates(blockedDates);
    offerRepository.save(availableOffer);
    offerRepository.save(nonAvailableOffer);
    Long expected = availableOffer.getOfferID();
    // act
    List<Offer> availableOffers = bookingServiceUnderTest.getAvailableOffers(
      LocalDate.now().plus(1, ChronoUnit.DAYS),
      LocalDate.now().plus(2, ChronoUnit.DAYS)
    );
    Long actual = availableOffers.get(0).getOfferID();
    // assert
    assertEquals(expected, actual);
  }

  @Test
  public void rejectShouldSetRejectedToTrue() {
    // arrange
    Booking booking = bookingRepository.save(
      new Booking(
        createUniqueUser(),
        createUniqueOffer(),
        LocalDate.now(),
        LocalDate.now().plus(1, ChronoUnit.DAYS)
      )
    );
    // act
    bookingServiceUnderTest.reject(booking.getId());
    // assert
    assertTrue(bookingRepository.findById(booking.getId()).get().isRejected());
  }
}
