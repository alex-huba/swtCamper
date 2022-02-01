package swtcamper.backend.services;


import javafx.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.backend.entities.*;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OfferServiceIntegrationsTest {

    @Autowired
    OfferService offerServiceUnderTest;

    @Autowired
    UserService userService;

    public User creatorUser() {
        return userService.create("Tester",
                "password",
                "a@a.de",
                "123456789",
                "Tester",
                "Tester",
                UserRole.OPERATOR,
                true);
    }

    @Test
    public void createOfferShouldAddOfferToDatabase() throws GenericServiceException {
        User creator = creatorUser();

        assertNotNull(creator);

        String title = "title";
        String location = "loc";
        String contact = "contact";
        String particularities = "p";
        long price = 0L;
        ArrayList<String> rentalConditions = new ArrayList<>();
        rentalConditions.add("a");
        rentalConditions.add("b");
        rentalConditions.add("c");
        ArrayList<Pair> blockedDates = new ArrayList<>();
        blockedDates.add(new Pair(LocalDate.now(), LocalDate.now().plusDays(5)));
        String make = "make";
        String model = "model";
        String year = "1998";
        String transmission = "transmission";
        int seats = 1;
        int beds = 1;
        boolean roofTent = false,
                roofRack = false,
                bikeRack = false,
                shower = false,
                toilet = false,
                kitchenUnit = false,
                fridge = false;

        Offer resultOffer = offerServiceUnderTest.create(creator,
                title,
                location,
                contact,
                particularities,
                price,
                rentalConditions,
                blockedDates,
                VehicleType.BUS,
                make,
                model,
                year,
                0L,
                0L,
                0L,
                FuelType.BENZIN,
                transmission,
                seats,
                beds,
                roofTent,
                roofRack,
                bikeRack,
                shower,
                toilet,
                kitchenUnit,
                fridge
        );

        assertNotNull(resultOffer);
        assertEquals(title, resultOffer.getTitle());
        assertEquals(location, resultOffer.getLocation());
        assertEquals(contact, resultOffer.getContact());
        assertEquals(particularities, resultOffer.getParticularities());
        assertEquals(price, resultOffer.getPrice());
        assertEquals(rentalConditions, resultOffer.getRentalConditions());

        Vehicle vehicle = resultOffer.getOfferedObject();
        assertNotNull(vehicle);


    }
}
