package swtcamper.backend.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.lang.management.GarbageCollectorMXBean;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.util.Pair;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.controller.LoggingController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceUnitTest {

  // ------------- Mocking -------------

  @InjectMocks
  private BookingService bookingServiceUnderTest;

  @Mock
  private OfferRepository offerRepository;

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private LoggingController loggingController;

  // Booking stuff

  public List<Booking> twoValidBookings() {
    List<Booking> twoValidBookings = new ArrayList<>();
    LocalDate startDate = createValidDateOfTodayPlusXDays(0);
    LocalDate endDate = createValidDateOfTodayPlusXDays(1);
    Booking booking1 = createValidBooking();
    LocalDate startDate1 = startDate.plus(2, ChronoUnit.DAYS);
    LocalDate endDate1 = endDate.plus(3, ChronoUnit.DAYS);
    Booking booking2 = new Booking(createEmptyUser(), createValidOffer(), startDate1, endDate1);
    twoValidBookings.add(booking1);
    twoValidBookings.add(booking2);
    return twoValidBookings;
  }

  private Booking createValidBooking() {
    Booking booking = new Booking(
            createEmptyUser(),
            createValidOffer(),
            createValidDateOfTodayPlusXDays(0),
            createValidDateOfTodayPlusXDays(1)
    );
    booking.setId(1L);
    return booking;
  }

  private Booking createValidActiveBooking() {
    Booking booking = new Booking(
            createEmptyUser(),
            createValidOffer(),
            createValidDateOfTodayPlusXDays(0),
            createValidDateOfTodayPlusXDays(1)
    );
    booking.setId(1L);
    booking.setActive(true);
    return booking;
  }

  public void mockFindAllBookings(List<Booking> bookings) {
    doReturn(bookings).when(bookingRepository).findAll();
  }

  public void mockSaveBooking(Booking booking) {
    doReturn(booking)
            .when(bookingRepository)
            .save(any());
  }

  public void mockFindBookingByIdReturnsEmptyOptional() {
    doReturn(Optional.empty()).when(bookingRepository).findById((any()));
  }

  public void mockFindBookingByIdReturnsFilledOptional() {
    doReturn(Optional.of(createValidBooking()))
            .when(bookingRepository)
            .findById(any());
  }

  public void mockFindBookingByIdReturnsFilledActiveOptional() {
    doReturn(Optional.of(createValidActiveBooking()))
            .when(bookingRepository)
            .findById(any());
  }

  public void mockBookingGetId(Booking booking) {
    doReturn(1L).when(booking).getId();
  }

  public void mockFindAllBookingsByIdReturnsListOf1Booking() {
    List<Booking> bookings = new ArrayList<>();
    bookings.add(createValidBooking());
    doReturn(bookings)
            .when(bookingRepository)
            .findAllById(any());
  }

  // Offer Stuff

  public Offer createValidOffer() {
    Offer offer = new Offer(
            new User(),
            new Vehicle(),
            "title",
            "location",
            "contact",
            "particularities",
            1,
            new ArrayList<String>(),
            new ArrayList<Pair>()
    );
    return offer;
  }

  public Offer createValidOfferWith1Booking() {
    Offer offer = createValidOffer();
    createValidOffer().getBookings().add(1L);
    return offer;
  }

  public void mockFindOfferByIdReturnsFilledOptional() {
    doReturn(Optional.of(createValidOffer()))
            .when(offerRepository)
            .findById(any());
  }

  public void mockFindOfferByIdReturnsFilledOptionalWithBooking() {
    doReturn(Optional.of(createValidOfferWith1Booking()))
            .when(offerRepository)
            .findById(any());
  }

  public void mockFindOfferByIdReturnsEmptyOptional() {
    doReturn(Optional.empty())
            .when(offerRepository)
            .findById(any());
  }

  public void mockFindOfferByIdReturnsOptionalWithBlockedDates() {
    Offer offer = createValidOffer();
    Pair pair = new Pair(createValidDateOfTodayPlusXDays(2), createValidDateOfTodayPlusXDays(3));
    ArrayList<Pair> blockedDates = new ArrayList<>();
    blockedDates.add(pair);
    offer.setBlockedDates(blockedDates);
    doReturn(Optional.of(offer))
            .when(offerRepository)
            .findById(any());
  }

  public void mockSaveOffer(Offer offer) {
    doReturn(offer)
            .when(offerRepository)
            .save(any());
  }

  public void mockFindAllOffersReturnsListWith1Offer() {
    List<Offer> offers = new ArrayList<>();
    offers.add(createValidOffer());
    doReturn(offers)
            .when(offerRepository)
            .findAll();
  }

  // User stuff

  public User createEmptyUser() {
    User user = new User();
    return user;
  }

  public UserDTO createEmptyUserDTO() {
    UserDTO userDTO = new UserDTO();
    return userDTO;
  }

  // LocalDates

  public LocalDate createValidDateOfTodayPlusXDays(int x) {
    LocalDate endDate = LocalDate.now().plus(x, ChronoUnit.DAYS);
    return endDate;
  }

  // ------------- Testing -------------

  // getAllBookings()

  @Test
  public void getAllBookingsShouldReturnAllBookings() {
    // arrange
    List<Booking> expected = twoValidBookings();
    mockFindAllBookings(expected);
    // act
    List<Booking> actual = bookingServiceUnderTest.getAllBookings();
    // assert
    assertEquals(expected, actual);
  }

  // create()

  /**
   * Checks if the create method calls bookingRepository.save() with the given arguments
   *
   * @throws GenericServiceException
   */
  @Test
  public void createShouldGiveBookingToRepository()
          throws GenericServiceException {
    // arrange
    mockSaveBooking(createValidBooking());
    mockFindBookingByIdReturnsFilledOptional();
    mockFindOfferByIdReturnsFilledOptional();
    mockSaveOffer(createValidOffer());
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(
            Booking.class
    );
    User user = createEmptyUser();
    Offer offer = createValidOffer();
    Booking expected = new Booking(
            user,
            offer,
            createValidDateOfTodayPlusXDays(0),
            createValidDateOfTodayPlusXDays(1)
    );
    // act
    Booking actual = bookingServiceUnderTest.create(
            user,
            offer,
            createValidDateOfTodayPlusXDays(0),
            createValidDateOfTodayPlusXDays(1)
    );
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertEquals(bookingArgumentCaptor.getValue().getRenter(), expected.getRenter());
    assertEquals(bookingArgumentCaptor.getValue().getOffer(), expected.getOffer());
    assertEquals(bookingArgumentCaptor.getValue().getStartDate(), expected.getStartDate());
    assertEquals(bookingArgumentCaptor.getValue().getEndDate(), expected.getEndDate());
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldThrowGSEIfOfferNotPresent() throws GenericServiceException {
    // arrange
    mockSaveBooking(createValidBooking());
    mockFindBookingByIdReturnsFilledOptional();
    mockFindOfferByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.create(createEmptyUser(),
            createValidOffer(),
            createValidDateOfTodayPlusXDays(0),
            createValidDateOfTodayPlusXDays(1));
  }

  // update()

  @Test(expected = GenericServiceException.class)
  public void updateShouldThrowGSEIfOfferNotPresent() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.update(
            1L,
            createValidDateOfTodayPlusXDays(0),
            createValidDateOfTodayPlusXDays(1),
            false,
            createEmptyUserDTO());
  }

  @Test(expected = GenericServiceException.class)
  public void updateShouldThrowGSEIfBookingIsActive() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledActiveOptional();
    // act
    bookingServiceUnderTest.update(1L,
            createValidDateOfTodayPlusXDays(0),
            createValidDateOfTodayPlusXDays(1),
            false,
            createEmptyUserDTO());
  }

  @Test
  public void updateShouldGiveUpdatedBookingToRepo() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledOptional();
    LocalDate newStartDate = createValidDateOfTodayPlusXDays(2);
    LocalDate newEndDate = createValidDateOfTodayPlusXDays(3);
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
    // act
    bookingServiceUnderTest.update(
            1L,
            newStartDate,
            newEndDate,
            true,
            new UserDTO()
    );
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertEquals(newStartDate, bookingArgumentCaptor.getValue().getStartDate());
    assertEquals(newEndDate, bookingArgumentCaptor.getValue().getEndDate());
    assertEquals(true, bookingArgumentCaptor.getValue().isActive());
  }

  //activate()

  @Test(expected = GenericServiceException.class)
  public void activateShouldThrowGSEIfBookingNotFound()
          throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.activate(1L, createEmptyUserDTO());
  }

  @Test
  public void activateShouldSetActiveToTrue() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledOptional();
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
    mockSaveBooking(createValidBooking());
    // act
    bookingServiceUnderTest.activate(2L, createEmptyUserDTO());
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertEquals(true, bookingArgumentCaptor.getValue().isActive());
  }

  // deactivate()

  @Test(expected = GenericServiceException.class)
  public void deactivateShouldThrowGSEIfBookingNotFound()
          throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.deactivate(1L, createEmptyUserDTO());
  }

  @Test
  public void deactivateShouldSetActiveToFalse() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledActiveOptional();
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
    // act
    bookingServiceUnderTest.deactivate(1L, new UserDTO());
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertEquals(false, bookingArgumentCaptor.getValue().isActive());
  }

  // delete()

  @Test(expected = GenericServiceException.class)
  public void deleteShouldThrowGSEIfBookingNotFound() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
  }

  @Test(expected = GenericServiceException.class)
  public void deleteShouldThrowGSEIfBookingIsActive() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledActiveOptional();
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
  }

  @Test(expected = GenericServiceException.class)
  public void deleteShouldThrowGSEIfOfferNotFound() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledOptional();
    mockFindOfferByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
  }

  @Test
  public void deleteShouldRemoveBookingIdFromOffer() throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledOptional();
    mockFindOfferByIdReturnsFilledOptionalWithBooking();
    ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
    // assert
    verify(offerRepository).save(offerArgumentCaptor.capture());
    assertEquals(false, offerArgumentCaptor.getValue().getBookings().contains(1L));
  }

  // getBookedDays()

  @Test(expected = GenericServiceException.class)
  public void getBookedDaysShouldThrowGSEIfOfferNotFound() throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.getBookedDays(1L);
  }

  @Test
  public void getBookedDaysShouldReturnCorrectResult() throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsOptionalWithBlockedDates();
    mockFindAllBookingsByIdReturnsListOf1Booking();
    List<LocalDate> expected = new ArrayList<>();
    expected.add(createValidDateOfTodayPlusXDays(0));
    expected.add(createValidDateOfTodayPlusXDays(1));
    expected.add(createValidDateOfTodayPlusXDays(2));
    expected.add(createValidDateOfTodayPlusXDays(3));
    // act
    List<LocalDate> actual = bookingServiceUnderTest.getBookedDays(1L);
    // assert
    assertIterableEquals(expected, actual);
  }

  @Test
  public void getAvailableOffersShouldReturnZeroOffers() throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsOptionalWithBlockedDates();
    mockFindAllBookingsByIdReturnsListOf1Booking();
    mockFindAllOffersReturnsListWith1Offer();
    // act
    List<Offer> actual = bookingServiceUnderTest.getAvailableOffers(createValidDateOfTodayPlusXDays(0), createValidDateOfTodayPlusXDays(2));
    // assert
    assertEquals(0, actual.size());
  }

  @Test
  public void getAvailableOffersShouldReturn1Offer() throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsOptionalWithBlockedDates();
    mockFindAllBookingsByIdReturnsListOf1Booking();
    mockFindAllOffersReturnsListWith1Offer();
    // act
    List<Offer> actual = bookingServiceUnderTest.getAvailableOffers(createValidDateOfTodayPlusXDays(5), createValidDateOfTodayPlusXDays(8));
    // assert
    assertEquals(1, actual.size());
  }


}

