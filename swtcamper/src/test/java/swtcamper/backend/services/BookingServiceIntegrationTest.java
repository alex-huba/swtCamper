package swtcamper.backend.services;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.api.controller.LoggingController;
import swtcamper.api.controller.ValidationHelper;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;

@RunWith(Parameterized.class)

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceIntegrationTest {

  @Autowired
  private BookingService bookingServiceUnderTest;

  @Autowired
  private BookingRepository bookingRepository;

  @Parameterized.Parameters
  public static ArrayList<List> provideBookingsForGetAllBookings() {
    Booking booking1 = new Booking();
    Booking booking2 = new Booking();
    Booking booking3 = new Booking();
    List<Booking> emptyList = new ArrayList<>();
    List<Booking> oneBookingList = List.of(booking1);
    List<Booking> twoBookingsList = List.of(booking1, booking2);
    List<Booking> threeBookingsList = List.of(booking1, booking2, booking3);
    ArrayList<List> parameters = new ArrayList<>();
    parameters.add(emptyList);
    parameters.add(oneBookingList);
    parameters.add(twoBookingsList);
    parameters.add(threeBookingsList);
    return parameters;
  }

  fun arrays()= listOf(listOf(listOf(1,2,3,4,5,6,7,8,9),3, listOf(4,6,8)))

  fun arrays()= arrayOf(listOf(listOf(1,2,3,4,5,6,7,8,9),3, listOf(4,6,8)))

  // ----- TESTING -----

  @ParameterizedTest
  @MethodSource("provideBookingsForGetAllBookings")
  public void getAllBookingsShouldReturnAllBookings(List<Booking> expected) {
    // arrange
    bookingRepository.saveAll(expected);
    // act
    List<Booking> actual = bookingServiceUnderTest.getAllBookings();
    // assert
    assertEquals(expected, actual);
  }

  @Test
  public void test() {
    assertEquals(true, true);
  }
}
