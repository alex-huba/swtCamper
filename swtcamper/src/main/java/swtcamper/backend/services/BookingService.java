package swtcamper.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public static List getBookedDays() {
        List<LocalDate> bookedDays = new ArrayList<>();
//        Optional<Offer> offerResponse = offerRepository.findById(offerID);
//        Offer offer = offerResponse.get();
//        ArrayList<Long> bookingIDs = offer.getBookings();
//        Iterable<Booking> bookings = bookingRepository.findAllById(bookingIDs);
        User user1 = new User();
        User user2 = new User();
        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        LocalDate startDate1 = LocalDate.of(2021, 12, 1);
        LocalDate endDate1 = LocalDate.of(2021, 12, 10);
        LocalDate startDate2 = LocalDate.of(2021, 10, 27);
        LocalDate endDate2 = LocalDate.of(2021, 11, 2);
        Long id1 = Long.valueOf(1);
        Long id2 = Long.valueOf(2);
        Booking booking1 = new Booking(id1, user1, offer1, startDate1, endDate1, true);
        Booking booking2 = new Booking(id2, user2, offer2, startDate2, endDate2, true);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

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
}
