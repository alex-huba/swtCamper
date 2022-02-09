package swtcamper.backend.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OfferServiceIntegrationsTest {

  @Autowired
  OfferService offerServiceUnderTest;

  @Autowired
  UserService userService;

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  OfferRepository offerRepository;

  public User creatorUser() {
    return userService.create(
      "Tester",
      "password",
      "a@a.de",
      "123456789",
      "Tester",
      "Tester",
      UserRole.OPERATOR,
      true
    );
  }

  public Offer testOffer(User creator) throws GenericServiceException {
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
    boolean roofTent = false, roofRack = false, bikeRack = false, shower =
      false, toilet = false, kitchenUnit = false, fridge = false;

    return offerServiceUnderTest.create(
      creator,
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
  }

  @Test
  public void createOfferShouldAddOfferToDatabase()
    throws GenericServiceException {
    User creator = creatorUser();
    assertNotNull(creator);

    Offer resultOffer = testOffer(creator);
    String title = "title";
    String location = "loc";
    String contact = "contact";
    String particularities = "p";
    long price = 0L;
    ArrayList<String> rentalConditions = new ArrayList<>();
    rentalConditions.add("a");
    rentalConditions.add("b");
    rentalConditions.add("c");

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

  @Test
  public void updateOfferShouldUpdateOfferToDatabase()
    throws GenericServiceException {
    User creator = creatorUser();

    assertNotNull(creator);

    Offer offer = testOffer(creator);

    String title = "updated_title";
    String location = "updated_loc";
    String contact = "updated_contact";
    String particularities = "updated_p";
    ArrayList<Long> bookings = new ArrayList<>();
    bookings.add(1L);
    bookings.add(2L);
    long price = 1L;
    boolean active = true;
    ArrayList<String> rentalConditions = new ArrayList<>();
    rentalConditions.add("e");
    rentalConditions.add("f");
    rentalConditions.add("g");
    ArrayList<Pair> blockedDates = new ArrayList<>();
    blockedDates.add(new Pair(LocalDate.now(), LocalDate.now().plusDays(5)));
    String make = "updated_make";
    String model = "updated_model";
    String year = "1999";
    String transmission = "updated_transmission";
    int seats = 2;
    int beds = 2;
    boolean roofTent = true, roofRack = true, bikeRack = true, shower =
      true, toilet = true, kitchenUnit = true, fridge = true;

    UserDTO user = modelMapper.userToUserDTO(creator);

    Offer resultOffer = offerServiceUnderTest.update(
      offer.getOfferID(),
      creator,
      offer.getOfferedObject(),
      title,
      location,
      contact,
      particularities,
      bookings,
      price,
      active,
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
      fridge,
      user
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

  @Test
  public void deleteOfferShouldDeleteOfferFromDatabase()
    throws GenericServiceException {
    User creator = creatorUser();
    assertNotNull(creator);
    UserDTO user = modelMapper.userToUserDTO(creator);

    Offer offer = testOffer(creator);
    assertNotNull(offer);
    //remove the Offer
    offerServiceUnderTest.delete(offer.getOfferID(), user);
    assertThrows(
      GenericServiceException.class,
      () -> offerServiceUnderTest.getOfferById(offer.getOfferID())
    );
  }

  @Test
  public void offersShouldReturnEmptyOfferListFromDatabase() {
    List<Offer> offers = offerServiceUnderTest.offers();

    assertEquals(0, offers.size());
  }

  @Test
  public void offersShouldReturnOfferListFromDatabase()
    throws GenericServiceException {
    User creator = creatorUser();
    testOffer(creator);

    List<Offer> offers = offerServiceUnderTest.offers();

    assertEquals(1, offers.size());
  }

  @Test
  public void getOffersByCreatedUserShouldReturnOffersCreatedByUser()
    throws GenericServiceException {
    User creator = creatorUser();
    testOffer(creator);
    testOffer(creator);
    List<Offer> expectedOffers = offerServiceUnderTest.offers();

    List<Offer> resultOffers = offerServiceUnderTest.getOffersCreatedByUser(
      creator
    );

    assertEquals(expectedOffers.size(), resultOffers.size());
  }

  @Test
  public void promoteOfferShouldReturnPromotedOffer()
    throws GenericServiceException {
    User creator = creatorUser();
    testOffer(creator);
    userService.setLoggedInUser(creator);
    List<Offer> offers = offerServiceUnderTest.offers();
    offerServiceUnderTest.promoteOffer(offers.get(0).getOfferID());

    offers = offerServiceUnderTest.offers();

    assertEquals(true, offers.get(0).isPromoted());
  }

  @Test
  public void degradeOfferShouldReturnPromotedOffer()
    throws GenericServiceException {
    User creator = creatorUser();
    testOffer(creator);
    userService.setLoggedInUser(creator);
    List<Offer> offers = offerServiceUnderTest.offers();
    offers.get(0).setPromoted(true);
    offerServiceUnderTest.degradeOffer(offers.get(0).getOfferID());

    offers = offerServiceUnderTest.offers();
    assertEquals(false, offers.get(0).isPromoted());
  }

  @Test
  public void getOfferByIdShouldReturnOfferWithSubmittedId()
    throws GenericServiceException {
    User creator = creatorUser();
    Offer offer = testOffer(creator);
    List<Offer> offers = offerServiceUnderTest.offers();
    long id = offers.get(0).getOfferID();

    assertEquals(
      offer.getOfferID(),
      offerServiceUnderTest.getOfferById(id).getOfferID()
    );
  }

  @Test
  public void getOfferByIdShouldReturnGenericExceptionForInvalidId()
    throws GenericServiceException {
    assertThrows(
      GenericServiceException.class,
      () -> offerServiceUnderTest.getOfferById(0)
    );
  }

  @Test
  public void getFilteredOffersShouldReturnFilteredOfferList()
    throws GenericServiceException {
    User creator = creatorUser();

    Offer offer1 = testOffer(creator);
    Offer offer2 = testOffer(creator);

    Filter fridgeIsTrue = new Filter();
    Filter fridgeIsFalse = new Filter();
    fridgeIsTrue.setFridge(true);
    fridgeIsFalse.setFridge(false);

    offer1.getOfferedObject().setFridge(true);
    offerRepository.save(offer1);
    offer2.getOfferedObject().setFridge(true);
    offerRepository.save(offer2);

    //assertEquals(2, offerServiceUnderTest.getFilteredOffers(fridgeIsTrue));
    assertEquals(
      2,
      offerServiceUnderTest.getFilteredOffers(fridgeIsFalse).size()
    );
  }

  @Test
  public void getBlockedDaysShouldReturnBlockedDays()
    throws GenericServiceException {
    User creator = creatorUser();
    Offer offer = testOffer(creator);

    List<LocalDate> expectedResult = new ArrayList<>();

    for (Pair pair : offer.getBlockedDates()) {
      LocalDate startDate = (LocalDate) pair.getKey();
      LocalDate endDate = (LocalDate) pair.getValue();
      long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
      for (int i = 0; i <= amountOfDays; i++) {
        expectedResult.add(startDate.plus(i, ChronoUnit.DAYS));
      }
    }

    assertEquals(
      expectedResult,
      offerServiceUnderTest.getBlockedDates(offer.getOfferID())
    );
  }

  @Test
  public void getBlockedDaysShouldReturnGenericServiceExceptionByNotFoundedOffer() {
    assertThrows(
      GenericServiceException.class,
      () -> offerServiceUnderTest.getBlockedDates(0)
    );
  }
}
