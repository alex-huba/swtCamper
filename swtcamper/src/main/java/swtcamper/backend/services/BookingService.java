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

    public List getBookedDays(long offerID) {
        List<LocalDate> bookedDays = new ArrayList<>();

        Optional<Offer> offerResponse = offerRepository.findById(offerID);
        Offer offer = offerResponse.get();
        ArrayList<Long> bookingIDs = offer.getBookings();
        Iterable<Booking> bookings = bookingRepository.findAllById(bookingIDs);

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

    public ArrayList<Long> getAvailableOffers(LocalDate startDate, LocalDate endDate) {
        // Liste aller angefragten Tage
        List<LocalDate> requestedDays = new ArrayList<>();
        long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= amountOfDays; i++) {
            requestedDays.add(startDate.plus(i, ChronoUnit.DAYS));
        }

        // Liste die returned wird
        ArrayList<Long> offerIDs = new ArrayList<>();

        // Alle Offers holen und die angefragten Tage gegen die bereits gebuchten Tage gegenchecken
        List<Offer> offerResponse = offerRepository.findAll();
        for (Offer offer : offerResponse) {
            List<LocalDate> bookedDays = getBookedDays(offer.getOfferID());
            for (LocalDate requestedDay : requestedDays) {
                if(!bookedDays.contains(requestedDay)) {
                  offerIDs.add(offer.getOfferID());
                };
            }
        }
        return offerIDs;
    }
}

