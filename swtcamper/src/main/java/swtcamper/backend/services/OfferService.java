package swtcamper.backend.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.LoggingController;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Service
public class OfferService {

  @Autowired
  OfferService offerService;

  @Autowired
  UserService userService;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private VehicleFeaturesRepository vehicleFeaturesRepository;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private LoggingController loggingController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private ModelMapper modelMapper;

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
    String engine,
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
    vehicleRepository.save(vehicle);

    VehicleFeatures vehicleFeatures = new VehicleFeatures(vehicle);
    setVehicleFeatures(
      vehicleFeatures,
      vehicleType,
      make,
      model,
      year,
      length,
      width,
      height,
      engine,
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
    vehicleFeaturesRepository.save(vehicleFeatures);

    vehicle.setVehicleFeatures(vehicleFeatures);

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
    long newVehicleId = vehicleRepository.save(vehicle).getVehicleID();
    loggingController.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "New vehicle with ID %s created by user %s.",
            newVehicleId,
            creator.getUsername()
          )
        )
      )
    );

    long newOfferId = offerRepository.save(offer).getOfferID();
    loggingController.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "New offer with ID %s created by user %s.",
            newOfferId,
            creator.getUsername()
          )
        )
      )
    );
    Optional<Offer> offerOptional = offerRepository.findById(newOfferId);
    if (offerOptional.isPresent()) {
      return offerOptional.get();
    } else {
      throw new GenericServiceException(
        "Newly created offer with ID: " + newOfferId + " not found."
      );
    }
  }

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
    String engine,
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
      if (booking.getOffer().getOfferID() == offerId && booking.isActive()) {
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
      Optional<VehicleFeatures> vehicleFeaturesResponse = vehicleFeaturesRepository.findById(
        vehicle.getVehicleFeatures().getVehicleFeaturesID()
      );
      if (vehicleFeaturesResponse.isPresent()) {
        VehicleFeatures vehicleFeatures = vehicleFeaturesResponse.get();
        setVehicleFeatures(
          vehicleFeatures,
          vehicleType,
          make,
          model,
          year,
          length,
          width,
          height,
          engine,
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
        vehicleFeaturesRepository.save(vehicleFeatures);

        vehicle.setVehicleFeatures(vehicleFeatures);
        vehicleRepository.save(vehicle);
        loggingController.log(
          modelMapper.LoggingMessageToLoggingMessageDTO(
            new LoggingMessage(
              LoggingLevel.INFO,
              String.format(
                "Vehicle with ID %s got updated by user %s.",
                vehicle.getVehicleID(),
                user.getUsername()
              )
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
          modelMapper.LoggingMessageToLoggingMessageDTO(
            new LoggingMessage(
              LoggingLevel.INFO,
              String.format(
                "Offer with ID %s got updated by user %s.",
                offer.getOfferID(),
                user.getUsername()
              )
            )
          )
        );
        return offerRepository.save(offer);
      } else {
        throw new GenericServiceException("VehicleFeatures not found.");
      }
    } else {
      throw new GenericServiceException("Vehicle not found.");
    }
  }

  private void setVehicleFeatures(
    VehicleFeatures vehicleFeatures,
    VehicleType vehicleType,
    String make,
    String model,
    String year,
    double length,
    double width,
    double height,
    String engine,
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
  ) {
    vehicleFeatures.setVehicleType(vehicleType);
    vehicleFeatures.setMake(make);
    vehicleFeatures.setModel(model);
    vehicleFeatures.setYear(year);
    vehicleFeatures.setLength(length);
    vehicleFeatures.setWidth(width);
    vehicleFeatures.setHeight(height);
    vehicleFeatures.setEngine(engine);
    vehicleFeatures.setTransmission(transmission);
    vehicleFeatures.setSeats(seats);
    vehicleFeatures.setBeds(beds);
    vehicleFeatures.setRoofTent(roofTent);
    vehicleFeatures.setRoofRack(roofRack);
    vehicleFeatures.setBikeRack(bikeRack);
    vehicleFeatures.setShower(shower);
    vehicleFeatures.setToilet(toilet);
    vehicleFeatures.setKitchenUnit(kitchenUnit);
    vehicleFeatures.setFridge(fridge);
  }

  /**
   * @param id ID of the offer to get deleted
   * @throws GenericServiceException if the given ID is not available
   */
  public void delete(long id, UserDTO user) throws GenericServiceException {
    // check if offer is in rent right now
    for (Booking booking : bookingController.getAllBookings()) {
      if (booking.getOffer().getOfferID() == id) {
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
        modelMapper.LoggingMessageToLoggingMessageDTO(
          new LoggingMessage(
            LoggingLevel.INFO,
            String.format(
              "Offer with ID %s got deleted by user %s.",
              id,
              user.getUsername()
            )
          )
        )
      );
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }

  public List<Offer> offers() {
    return offerRepository.findAll();
  }

  /**
   * Gets all days from the blockedDays list of a given offer (startDate, endDate, all days in between)
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
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "Offer with ID %s got promoted by operator %s.",
            offerID,
            userService.getLoggedInUser().getUsername()
          )
        )
      )
    );
    return offerRepository.save(offer);
  }

  public Offer degradeOffer(long offerID) throws GenericServiceException {
    Offer offer = getOfferById(offerID);
    offer.setPromoted(false);
    loggingController.log(
      modelMapper.LoggingMessageToLoggingMessageDTO(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "Offer with ID %s got degraded by operator %s.",
            offerID,
            userService.getLoggedInUser().getUsername()
          )
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
