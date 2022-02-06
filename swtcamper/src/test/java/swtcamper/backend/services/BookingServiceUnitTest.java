package swtcamper.backend.services;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

  // ------------- MOCKING -------------

  @InjectMocks
  private BookingService bookingServiceUnderTest;

  @Mock
  private OfferRepository offerRepository;

  @Mock
  private BookingRepository bookingRepository;

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
    Booking booking2 = new Booking(
      createEmptyUser(),
      createValidOffer(),
      startDate1,
      endDate1
    );
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
    doReturn(booking).when(bookingRepository).save(any());
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

  public void mockFindAllBookingsByIdReturnsListOf1Booking() {
    List<Booking> bookings = new ArrayList<>();
    bookings.add(createValidBooking());
    doReturn(bookings).when(bookingRepository).findAllById(any());
  }

  public void mockGetAllBookings(List<Booking> bookings) {
    doReturn(bookings).when(bookingRepository).findAll();
  }

  // Offer Stuff

  public Offer createValidOffer() {
    return new Offer(
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
    doReturn(Optional.empty()).when(offerRepository).findById(any());
  }

  public void mockFindOfferByIdReturnsOptionalWithBlockedDates() {
    Offer offer = createValidOffer();
    Pair pair = new Pair(
      createValidDateOfTodayPlusXDays(2),
      createValidDateOfTodayPlusXDays(3)
    );
    ArrayList<Pair> blockedDates = new ArrayList<>();
    blockedDates.add(pair);
    offer.setBlockedDates(blockedDates);
    doReturn(Optional.of(offer)).when(offerRepository).findById(any());
  }

  public void mockSaveOffer(Offer offer) {
    doReturn(offer).when(offerRepository).save(any());
  }

  public void mockFindAllOffersReturnsListWith1Offer() {
    List<Offer> offers = new ArrayList<>();
    offers.add(createValidOffer());
    doReturn(offers).when(offerRepository).findAll();
  }

  // User stuff

  public User createEmptyUser() {
    return new User();
  }

  public UserDTO createEmptyUserDTO() {
    return new UserDTO();
  }

  // LocalDates

  public LocalDate createValidDateOfTodayPlusXDays(int x) {
    return LocalDate.now().plus(x, ChronoUnit.DAYS);
  }

  // ------------- TESTING -------------

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

  @Test
  public void getBookingsForUserShouldReturnBookingsOfUser() {
    // arrange
    Booking booking1 = new Booking();
    Booking booking2 = new Booking();
    User user1 = createEmptyUser();
    User user2 = createEmptyUser();
    user1.setId(1L);
    user2.setId(2L);
    Offer offer1 = new Offer();
    Offer offer2 = new Offer();
    offer1.setCreator(user1);
    offer2.setCreator(user2);
    booking1.setOffer(offer1);
    booking2.setOffer(offer2);
    List<Booking> bookings = new ArrayList<>();
    bookings.add(booking1);
    bookings.add(booking2);
    mockGetAllBookings(bookings);
    // act
    List<Booking> actual = bookingServiceUnderTest.getBookingsForUser(user1);
    // assert
    assertEquals(1L, (long) actual.get(0).getOffer().getCreator().getId());
  }

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
    bookingServiceUnderTest.create(
      user,
      offer,
      createValidDateOfTodayPlusXDays(0),
      createValidDateOfTodayPlusXDays(1)
    );
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertEquals(
      bookingArgumentCaptor.getValue().getRenter(),
      expected.getRenter()
    );
    assertEquals(
      bookingArgumentCaptor.getValue().getOffer(),
      expected.getOffer()
    );
    assertEquals(
      bookingArgumentCaptor.getValue().getStartDate(),
      expected.getStartDate()
    );
    assertEquals(
      bookingArgumentCaptor.getValue().getEndDate(),
      expected.getEndDate()
    );
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldThrowGSEIfOfferNotPresent()
    throws GenericServiceException {
    // arrange
    mockSaveBooking(createValidBooking());
    mockFindBookingByIdReturnsFilledOptional();
    mockFindOfferByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.create(
      createEmptyUser(),
      createValidOffer(),
      createValidDateOfTodayPlusXDays(0),
      createValidDateOfTodayPlusXDays(1)
    );
  }

  @Test(expected = GenericServiceException.class)
  public void updateShouldThrowGSEIfOfferNotPresent()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.update(
      1L,
      createValidDateOfTodayPlusXDays(0),
      createValidDateOfTodayPlusXDays(1),
      false,
      createEmptyUserDTO()
    );
  }

  @Test(expected = GenericServiceException.class)
  public void updateShouldThrowGSEIfBookingIsActive()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledActiveOptional();
    // act
    bookingServiceUnderTest.update(
      1L,
      createValidDateOfTodayPlusXDays(0),
      createValidDateOfTodayPlusXDays(1),
      false,
      createEmptyUserDTO()
    );
  }

  @Test
  public void updateShouldGiveUpdatedBookingToRepo()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledOptional();
    LocalDate newStartDate = createValidDateOfTodayPlusXDays(2);
    LocalDate newEndDate = createValidDateOfTodayPlusXDays(3);
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(
      Booking.class
    );
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
    assertTrue(bookingArgumentCaptor.getValue().isActive());
  }

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
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(
      Booking.class
    );
    mockSaveBooking(createValidBooking());
    // act
    bookingServiceUnderTest.activate(2L, createEmptyUserDTO());
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertTrue(bookingArgumentCaptor.getValue().isActive());
  }

  @Test(expected = GenericServiceException.class)
  public void deactivateShouldThrowGSEIfBookingNotFound()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.deactivate(1L, createEmptyUserDTO());
  }

  @Test
  public void deactivateShouldSetActiveToFalse()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledActiveOptional();
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(
      Booking.class
    );
    // act
    bookingServiceUnderTest.deactivate(1L, new UserDTO());
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertFalse(bookingArgumentCaptor.getValue().isActive());
  }

  @Test(expected = GenericServiceException.class)
  public void deleteShouldThrowGSEIfBookingNotFound()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
  }

  @Test(expected = GenericServiceException.class)
  public void deleteShouldThrowGSEIfBookingIsActive()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledActiveOptional();
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
  }

  @Test(expected = GenericServiceException.class)
  public void deleteShouldThrowGSEIfOfferNotFound()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledOptional();
    mockFindOfferByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
  }

  @Test
  public void deleteShouldRemoveBookingIdFromOffer()
    throws GenericServiceException {
    // arrange
    mockFindBookingByIdReturnsFilledOptional();
    mockFindOfferByIdReturnsFilledOptionalWithBooking();
    ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(
      Offer.class
    );
    // act
    bookingServiceUnderTest.delete(1L, new UserDTO());
    // assert
    verify(offerRepository).save(offerArgumentCaptor.capture());
    assertFalse(offerArgumentCaptor.getValue().getBookings().contains(1L));
  }

  @Test(expected = GenericServiceException.class)
  public void getBookedDaysShouldThrowGSEIfOfferNotFound()
    throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsEmptyOptional();
    // act
    bookingServiceUnderTest.getBookedAndBlockedDays(1L);
  }

  @Test
  public void getBookedDaysShouldReturnCorrectResult()
    throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsOptionalWithBlockedDates();
    mockFindAllBookingsByIdReturnsListOf1Booking();
    List<LocalDate> expected = new ArrayList<>();
    expected.add(createValidDateOfTodayPlusXDays(0));
    expected.add(createValidDateOfTodayPlusXDays(1));
    expected.add(createValidDateOfTodayPlusXDays(2));
    expected.add(createValidDateOfTodayPlusXDays(3));
    // act
    List<LocalDate> actual = bookingServiceUnderTest.getBookedAndBlockedDays(
      1L
    );
    // assert
    assertIterableEquals(expected, actual);
  }

  @Test
  public void getAvailableOffersShouldReturnZeroOffers()
    throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsOptionalWithBlockedDates();
    mockFindAllBookingsByIdReturnsListOf1Booking();
    mockFindAllOffersReturnsListWith1Offer();
    // act
    List<Offer> actual = bookingServiceUnderTest.getAvailableOffers(
      createValidDateOfTodayPlusXDays(0),
      createValidDateOfTodayPlusXDays(2)
    );
    // assert
    assertEquals(0, actual.size());
  }

  @Test
  public void getAvailableOffersShouldReturn1Offer()
    throws GenericServiceException {
    // arrange
    mockFindOfferByIdReturnsOptionalWithBlockedDates();
    mockFindAllBookingsByIdReturnsListOf1Booking();
    mockFindAllOffersReturnsListWith1Offer();
    // act
    List<Offer> actual = bookingServiceUnderTest.getAvailableOffers(
      createValidDateOfTodayPlusXDays(5),
      createValidDateOfTodayPlusXDays(8)
    );
    // assert
    assertEquals(1, actual.size());
  }
}
