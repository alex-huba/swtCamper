package swtcamper.backend.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.lang.management.GarbageCollectorMXBean;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
  private LoggingController loggingController;

  @Mock
  private ModelMapper modelMapper;

  private User testUser = new User();
  private UserDTO testUserDTO = new UserDTO();
  private LocalDate testStartDate = LocalDate.now();
  private LocalDate testEndDate = LocalDate.now().plus(1, ChronoUnit.DAYS);

  private Offer testOffer = new Offer(
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

  public void mockFindAll(List<Booking> bookings) {
    doReturn(bookings).when(bookingRepository).findAll();
  }

  //    public void mockSave(Booking booking) {
  //        doReturn(booking)
  //                .when(bookingRepository)
  //                .save(booking);
  //    }

  // TODO Booking is not mock
  public void mockBookingGetId(Booking booking) {
    doReturn(-1).when(booking).getId();
  }

  public void mockFindById() {
    doReturn(null).when(bookingRepository).save(any());
  }

  public List<Booking> twoValidBookings() {
    List<Booking> twoValidBookings = new ArrayList<>();
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
    Booking booking1 = new Booking(new User(), testOffer, startDate, endDate);
    LocalDate startDate1 = LocalDate.now().plus(2, ChronoUnit.DAYS);
    LocalDate endDate1 = LocalDate.now().plus(3, ChronoUnit.DAYS);
    Booking booking2 = new Booking(new User(), testOffer, startDate1, endDate1);
    twoValidBookings.add(booking1);
    twoValidBookings.add(booking2);
    return twoValidBookings;
  }

  private Booking testBooking = new Booking(
    testUser,
    testOffer,
    testStartDate,
    testEndDate
  );

  // ------------- Tests -------------

  @Test
  public void getAllBookingsShouldReturnAllBookings() {
    // arrange
    List<Booking> expected = twoValidBookings();
    mockFindAll(expected);
    // act
    List<Booking> actual = bookingServiceUnderTest.getAllBookings();
    // assert
    assertEquals(expected, actual);
  }

  @Test
  public void createShouldGiveBookingToRepository()
    throws GenericServiceException {
    // arrange
    Booking expected = testBooking;
    mockBookingGetId(testBooking);
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(
      Booking.class
    );
    // act
    Booking actual = bookingServiceUnderTest.create(
      testUser,
      testOffer,
      LocalDate.now(),
      LocalDate.now().plus(1, ChronoUnit.DAYS),
      false
    );
    // assert
    verify(bookingRepository).save(bookingArgumentCaptor.capture());
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test(expected = GenericServiceException.class)
  public void activateShouldThrowGSEIfBookingNotFound()
    throws GenericServiceException {
    // arrange
    mockFindById();
    // act
    bookingServiceUnderTest.activate(1L, testUserDTO);
  }

  {}
}
