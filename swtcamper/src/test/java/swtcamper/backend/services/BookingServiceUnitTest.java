package swtcamper.backend.services;

import javafx.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.api.ModelMapper;
import swtcamper.api.controller.LoggingController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.Offer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceUnitTest {

    // ------------- Mocking -------------

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private LoggingController loggingController;

    @Mock
    private ModelMapper modelMapper;

    public void mockFindAll(List<Booking> bookings) {
        doReturn(bookings)
                .when(bookingRepository)
                .findAll();
    }

    public List<Booking> twoValidBookings() {
        List<Booking> twoValidBookings = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2022, 1, 16);
        LocalDate endDate = LocalDate.of(2022, 1, 16);
        Booking booking1 = new Booking(
                new User(),
                testOffer,
                startDate,
                endDate
        );
        Booking booking2 = new Booking(
                new User(),
                testOffer,
                startDate,
                endDate
        );
        twoValidBookings.add(booking1);
        twoValidBookings.add(booking2);
        return twoValidBookings;
    }

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

    // ------------- Tests -------------

    @Test
    public void getAllBookingsShouldReturnAllBookings() {
        // arrange
        List<Booking> expected = twoValidBookings();
        mockFindAll(expected);
        // act
        List<Booking> actual = bookingService.getAllBookings();
        // assert
        assertEquals(expected, actual);
    }



}
