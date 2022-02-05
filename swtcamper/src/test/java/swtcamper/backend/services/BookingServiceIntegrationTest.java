package swtcamper.backend.services;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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



@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceIntegrationTest {

  @Autowired
  private BookingService bookingServiceUnderTest;

  @Autowired
  private BookingRepository bookingRepository;

  // ----- TESTING -----

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
    assertThat(actualBooking).usingRecursiveComparison()
            .isEqualTo(expectedBooking);
  }

//  @Test
//  public void test() {
//    assertEquals(true, true);
//  }
}
