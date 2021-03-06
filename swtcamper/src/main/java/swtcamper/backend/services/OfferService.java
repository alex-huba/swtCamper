package swtcamper.backend.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.contract.interfaces.IBookingController;
import swtcamper.api.contract.interfaces.ILoggingController;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Service
public class OfferService {

  @Autowired
  private UserService userService;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private ILoggingController loggingController;

  @Autowired
  private IBookingController bookingController;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * Creates a new offer
   *
   * @param creator
   * @param title
   * @param location
   * @param contact
   * @param particularities
   * @param price
   * @param rentalConditions
   * @param blockedDates
   * @param vehicleType
   * @param make
   * @param model
   * @param year
   * @param length
   * @param width
   * @param height
   * @param fuelType
   * @param transmission
   * @param seats
   * @param beds
   * @param roofTent
   * @param roofRack
   * @param bikeRack
   * @param shower
   * @param toilet
   * @param kitchenUnit
   * @param fridge
   * @return created offer
   * @throws GenericServiceException
   */
  public Offer create(
    // TODO validation
    User creator,
    // Offer-Parameter
    String title,
    String location,
    String contact,
    String particularities,
    long price,
    ArrayList<String> rentalConditions,
    ArrayList<Pair> blockedDates,
    //VehicleFeatures-Parameter
    VehicleType vehicleType,
    String make,
    String model,
    String year,
    double length,
    double width,
    double height,
    FuelType fuelType,
    String transmission,
    int seats,
    int beds,
    boolean roofTent,
    boolean roofRack,
    boolean bikeRack,
    boolean shower,
    boolean toilet,
    boolean kitchenUnit,
    boolean fridge
  ) throws GenericServiceException {
    if (!creator.isEnabled() || creator.isLocked()) {
      throw new GenericServiceException(
        "User cannot create new offers as long as he/she is locked or not enabled."
      );
    }

    Vehicle vehicle = new Vehicle();
    vehicle.setVehicleType(vehicleType);
    vehicle.setMake(make);
    vehicle.setModel(model);
    vehicle.setYear(year);
    vehicle.setLength(length);
    vehicle.setWidth(width);
    vehicle.setHeight(height);
    vehicle.setFuelType(fuelType);
    vehicle.setTransmission(transmission);
    vehicle.setSeats(seats);
    vehicle.setBeds(beds);
    vehicle.setRoofTent(roofTent);
    vehicle.setRoofRack(roofRack);
    vehicle.setBikeRack(bikeRack);
    vehicle.setShower(shower);
    vehicle.setToilet(toilet);
    vehicle.setKitchenUnit(kitchenUnit);
    vehicle.setFridge(fridge);

    vehicleRepository.save(vehicle);

    Offer offer = new Offer(
      creator,
      vehicle,
      title,
      location,
      contact,
      particularities,
      price,
      rentalConditions,
      blockedDates
    );

    long newOfferId = offerRepository.save(offer).getOfferID();
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "New offer with ID %s created by user %s.",
          newOfferId,
          creator.getUsername()
        )
      )
    );
    Optional<Offer> offerOptional = offerRepository.findById(newOfferId);
    Optional<Vehicle> vehicleOptional = vehicleRepository.findById(
      offerOptional.get().getOfferedObject().getVehicleID()
    );
    long newVehicleId = vehicleOptional.get().getVehicleID();
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "New vehicle with ID %s created by user %s.",
          newVehicleId,
          creator.getUsername()
        )
      )
    );

    return offerOptional.get();
  }

  /**
   * Updates an offer with new values
   *
   * @param offerId
   * @param creator
   * @param offeredObject
   * @param title
   * @param location
   * @param contact
   * @param particularities
   * @param bookings
   * @param price
   * @param active
   * @param rentalConditions
   * @param blockedDates
   * @param vehicleType
   * @param make
   * @param model
   * @param year
   * @param length
   * @param width
   * @param height
   * @param fuelType
   * @param transmission
   * @param seats
   * @param beds
   * @param roofTent
   * @param roofRack
   * @param bikeRack
   * @param shower
   * @param toilet
   * @param kitchenUnit
   * @param fridge
   * @param user
   * @return updated offer
   * @throws GenericServiceException
   */
  public Offer update(
    long offerId,
    User creator,
    Vehicle offeredObject,
    // Offer-Parameter
    String title,
    String location,
    String contact,
    String particularities,
    ArrayList<Long> bookings,
    long price,
    boolean active,
    ArrayList<String> rentalConditions,
    ArrayList<Pair> blockedDates,
    //VehicleFeatures-Parameter
    VehicleType vehicleType,
    String make,
    String model,
    String year,
    double length,
    double width,
    double height,
    FuelType fuelType,
    String transmission,
    int seats,
    int beds,
    boolean roofTent,
    boolean roofRack,
    boolean bikeRack,
    boolean shower,
    boolean toilet,
    boolean kitchenUnit,
    boolean fridge,
    UserDTO user
  ) throws GenericServiceException {
    if (!user.isEnabled() || user.isLocked()) {
      throw new GenericServiceException(
        "User cannot update offers as long as he/she is locked or not enabled."
      );
    }

    Optional<Offer> offerResponse = offerRepository.findById(offerId);
    Offer offer;
    if (offerResponse.isPresent()) {
      offer = offerResponse.get();
    } else {
      throw new GenericServiceException(
        "There is no offer with specified ID " + offerId
      );
    }

    // check if offer is in rent right now
    for (Booking booking : bookingController.getAllBookings()) {
      if (
        booking.getOffer().getOfferID() == offerId &&
        booking.isActive() &&
        !booking.isRejected()
      ) {
        throw new GenericServiceException(
          "Cannot modify offer with ID " +
          offerId +
          " while it is part of an active booking."
        );
      }
    }

    Optional<Vehicle> vehicleResponse = vehicleRepository.findById(
      offeredObject.getVehicleID()
    );
    if (vehicleResponse.isPresent()) {
      Vehicle vehicle = vehicleResponse.get();
      vehicle.setVehicleType(vehicleType);
      vehicle.setMake(make);
      vehicle.setModel(model);
      vehicle.setYear(year);
      vehicle.setLength(length);
      vehicle.setWidth(width);
      vehicle.setHeight(height);
      vehicle.setFuelType(fuelType);
      vehicle.setTransmission(transmission);
      vehicle.setSeats(seats);
      vehicle.setBeds(beds);
      vehicle.setRoofTent(roofTent);
      vehicle.setRoofRack(roofRack);
      vehicle.setBikeRack(bikeRack);
      vehicle.setShower(shower);
      vehicle.setToilet(toilet);
      vehicle.setKitchenUnit(kitchenUnit);
      vehicle.setFridge(fridge);
      vehicleRepository.save(vehicle);
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "Vehicle with ID %s got updated by user %s.",
            vehicle.getVehicleID(),
            user.getUsername()
          )
        )
      );

      offer.setCreator(creator);
      offer.setOfferedObject(vehicle);
      offer.setBookings(bookings);
      offer.setTitle(title);
      offer.setLocation(location);
      offer.setContact(contact);
      offer.setParticularities(particularities);
      offer.setPrice(price);
      offer.setActive(active);
      offer.setRentalConditions(rentalConditions);
      offer.setBlockedDates(blockedDates);
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "Offer with ID %s got updated by user %s.",
            offer.getOfferID(),
            user.getUsername()
          )
        )
      );
      return offerRepository.save(offer);
    } else {
      throw new GenericServiceException("Vehicle not found.");
    }
  }

  /**
   * Deletes a specific offer from the database (Warning: This is not constructive for Offer-History)
   *
   * @param id ID of the offer to get deleted
   * @throws GenericServiceException if the given ID is not available
   */
  public void delete(long id, UserDTO user) throws GenericServiceException {
    // check if offer is in rent right now
    for (Booking booking : bookingController.getAllBookings()) {
      if (
        booking.getOffer().getOfferID() == id &&
        booking.isActive() &&
        !booking.isRejected()
      ) {
        throw new GenericServiceException(
          "Cannot modify offer with ID " +
          id +
          " while it is part of an active booking."
        );
      }
    }
    try {
      offerRepository.deleteById(id);
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "Offer with ID %s got deleted by user %s.",
            id,
            user.getUsername()
          )
        )
      );
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }

  /**
   * Gets a list of all available offers
   *
   * @return list of all available offers
   */
  public List<Offer> offers() {
    return offerRepository.findAll();
  }

  /**
   * Gets all offers from the database that were created by a user
   *
   * @param user {@link User} whose offers shall be searched
   * @return List of OfferDTOs of offers that were created by the user
   * @throws GenericServiceException
   */
  public List<Offer> getOffersCreatedByUser(User user) {
    return offers()
      .stream()
      .filter(offerDTO -> offerDTO.getCreator().getId().equals(user.getId()))
      .collect(Collectors.toList());
  }

  /**
   * Looks for fitting offers in the database
   *
   * @param filter {@link Filter} to hold search settings.
   * @return (Array)List of offers that fit to the given Filter
   * @throws GenericServiceException
   */
  public List<Offer> getFilteredOffers(Filter filter)
    throws GenericServiceException {
    return filter.isEmpty()
      ? offers()
      : modelMapper.offerDTOsToOffer(
        bookingController
          .getAvailableOffers(filter.getStartDate(), filter.getEndDate())
          .stream()
          .filter(offerDTO ->
            (
              filter.getLocation() == null ||
              offerDTO
                .getLocation()
                .toLowerCase()
                .contains(filter.getLocation().toLowerCase())
            ) &&
            (
              filter.getVehicleType() == null ||
              offerDTO
                .getOfferedObject()
                .getVehicleType()
                .equals(filter.getVehicleType())
            ) &&
            (
              filter.getVehicleBrand() == null ||
              offerDTO
                .getOfferedObject()
                .getMake()
                .toLowerCase()
                .contains(filter.getVehicleBrand().toLowerCase())
            ) &&
            (
              filter.getConstructionYear() == 0 ||
              Integer.parseInt(offerDTO.getOfferedObject().getYear()) >=
              filter.getConstructionYear()
            ) &&
            (
              filter.getMaxPricePerDay() == 0 ||
              offerDTO.getPrice() <= filter.getMaxPricePerDay()
            ) &&
            (
              (
                filter.getFuelType() == null ||
                (
                  !offerDTO
                    .getOfferedObject()
                    .getVehicleType()
                    .equals(VehicleType.TRAILER) &&
                  offerDTO
                    .getOfferedObject()
                    .getFuelType()
                    .equals(filter.getFuelType())
                )
              )
            ) &&
            (
              filter.getTransmissionType() == null ||
              (
                !offerDTO
                  .getOfferedObject()
                  .getVehicleType()
                  .equals(VehicleType.TRAILER) &&
                offerDTO
                  .getOfferedObject()
                  .getTransmission()
                  .toUpperCase()
                  .equals(filter.getTransmissionType().toString())
              )
            ) &&
            (
              filter.getSeatAmount() == 0 ||
              offerDTO.getOfferedObject().getSeats() >= filter.getSeatAmount()
            ) &&
            (
              filter.getBedAmount() == 0 ||
              offerDTO.getOfferedObject().getBeds() >= filter.getBedAmount()
            ) &&
            evalCheckBoxes(offerDTO, filter)
          )
          .collect(Collectors.toList())
      );
  }

  /**
   * Evaluates whether one of the checkboxes in the offer does not equal to its value in the filter
   *
   * @param offerDTO Offer that shall be looked in
   * @param filter   {@link Filter} that holds the settings of the checkboxes
   * @return true if all checkboxes in the offer equal to their values in the filter, false if there is at least one that does not
   */
  private boolean evalCheckBoxes(OfferDTO offerDTO, Filter filter) {
    List<Boolean> booleanList = new ArrayList<>();

    if (filter.isRoofTent()) booleanList.add(
      offerDTO.getOfferedObject().isRoofTent()
    );
    if (filter.isRoofRack()) booleanList.add(
      offerDTO.getOfferedObject().isRoofRack()
    );
    if (filter.isBikeRack()) booleanList.add(
      offerDTO.getOfferedObject().isBikeRack()
    );
    if (filter.isShower()) booleanList.add(
      offerDTO.getOfferedObject().isShower()
    );
    if (filter.isToilet()) booleanList.add(
      offerDTO.getOfferedObject().isToilet()
    );
    if (filter.isKitchen()) booleanList.add(
      offerDTO.getOfferedObject().isKitchenUnit()
    );
    if (filter.isFridge()) booleanList.add(
      offerDTO.getOfferedObject().isFridge()
    );
    return !booleanList.contains(false);
  }

  /**
   * Gets all days from the blockedDays list of a given offer (startDate, endDate, all days in between)
   *
   * @param offerID
   * @return
   * @throws GenericServiceException
   */
  public List<LocalDate> getBlockedDates(long offerID)
    throws GenericServiceException {
    List<LocalDate> blockedDates = new ArrayList<>();

    Optional<Offer> offerResponse = offerRepository.findById(offerID);
    if (offerResponse.isPresent()) {
      Offer offer = offerResponse.get();

      for (Pair pair : offer.getBlockedDates()) {
        LocalDate startDate = (LocalDate) pair.getKey();
        LocalDate endDate = (LocalDate) pair.getValue();
        long amountOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= amountOfDays; i++) {
          blockedDates.add(startDate.plus(i, ChronoUnit.DAYS));
        }
      }
      return blockedDates;
    }
    throw new GenericServiceException(
      "Offer with following ID not found: " + offerID
    );
  }

  public Offer promoteOffer(long offerID) throws GenericServiceException {
    Offer offer = getOfferById(offerID);
    offer.setPromoted(true);
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "Offer with ID %s got promoted by operator %s.",
          offerID,
          userService.getLoggedInUser().getUsername()
        )
      )
    );
    return offerRepository.save(offer);
  }

  public Offer degradeOffer(long offerID) throws GenericServiceException {
    Offer offer = getOfferById(offerID);
    offer.setPromoted(false);
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "Offer with ID %s got degraded by operator %s.",
          offerID,
          userService.getLoggedInUser().getUsername()
        )
      )
    );
    return offerRepository.save(offer);
  }

  public Offer getOfferById(long offerID) throws GenericServiceException {
    Optional<Offer> offerOptional = offerRepository.findById(offerID);
    if (offerOptional.isPresent()) {
      return offerOptional.get();
    }
    throw new GenericServiceException(
      "There is no offer with ID " + offerID + "."
    );
  }
}
