package swtcamper.backend.services;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.repositories.BookingRepository;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingServiceUnderTest;

    @Autowired
    private BookingRepository bookingRepository;

    private static Stream<List<Booking>> provideBookingsForGetAllBookings() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        Booking booking3 = new Booking();
        List<Booking> emptyList = new ArrayList<>();
        List<Booking> oneBookingList = List.of(booking1);
        List<Booking> twoBookingsList = List.of(booking1, booking2);
        List<Booking> threeBookingsList = List.of(booking1, booking2, booking3);
        return Stream.of(
                emptyList,
                oneBookingList,
                twoBookingsList,
                threeBookingsList
        );
    }
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

}
