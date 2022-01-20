package swtcamper.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.interfaces.IOfferController;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class OfferController implements IOfferController {

  @Autowired
  private OfferService offerService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private VehicleFeaturesRepository vehicleFeaturesRepository;

  @Autowired
  private UserController userController;

  @Autowired
  private BookingService bookingService;

  /**
   * Get a List of OfferDTOs of all available offers in the database
   * @return List of OfferDTOs
   * @throws GenericServiceException
   */
  public List<OfferDTO> offers() throws GenericServiceException {
    return modelMapper.offersToOfferDTOs(offerService.offers());
  }

  /**
   * Get a specific offer by its ID
   * @param id ID of the wanted offer
   * @return wanted Offer
   * @throws GenericServiceException
   */
  public Offer getOfferById(long id) throws GenericServiceException {
    Optional<Offer> offerResult = offerRepository.findById(id);
    if (offerResult.isPresent()) {
      return offerResult.get();
    }
    throw new GenericServiceException(
      String.format("Offer with ID %s could not be found.", id)
    );
  }

  /**
   * Creates a new offer and forwards it to the {@link OfferService} where it gets saved to the database
   * @param title of the new offer
   * @param location where the {@link Vehicle} can be picked up from
   * @param contact How the provider can be reached
   * @param particularities Any points that should be said about the offer
   * @param price per day for the vehicle
   * @param rentalConditions List of (String) conditions that are wanted by the provider
   * @param vehicleType {@link VehicleType} of the offered {@link Vehicle}
   * @param make brand of the offered {@link Vehicle}
   * @param model model of the offered {@link Vehicle}
   * @param year in which the offered {@link Vehicle} was produced
   * @param length of the vehicle in cm
   * @param width of the vehicle in cm
   * @param height of the vehicle in cm
   * @param fuelType Rather specifies the vehicle's fuel type
   * @param transmission {@link swtcamper.backend.entities.TransmissionType} of the offered vehicle
   * @param seats Amount of seats that the offered vehicle has
   * @param beds Amount of beds that the offered vehicle has
   * @param roofTent Does the vehicle have a roof tent?
   * @param roofRack Does the vehicle have a roof rack?
   * @param bikeRack Does the vehicle have a bike rack?
   * @param shower Does the vehicle have a shower?
   * @param toilet Does the vehicle have a toilet?
   * @param kitchenUnit Does the vehicle have a kitchen unit?
   * @param fridge Does the vehicle have a fridge?
   * @return {@link OfferDTO} of the new offer
   */
  public OfferDTO create(
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
    return modelMapper.offerToOfferDTO(
      offerService.create(
        userController.getLoggedInUser(),
        //Offer-Parameter
        title,
        location,
        contact,
        particularities,
        price,
        rentalConditions,
        blockedDates,
        //VehicleFeatures-Parameter
        vehicleType,
        make,
        model,
        year,
        length,
        width,
        height,
        fuelType,
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
      )
    );
  }

  /**
   * Updates an existing offer and forwards it to the {@link OfferService} where it gets saved to the database
   * @param title of the new offer
   * @param location where the {@link Vehicle} can be picked up from
   * @param contact How the provider can be reached
   * @param particularities Any points that should be said about the offer
   * @param price per day for the vehicle
   * @param rentalConditions List of (String) conditions that are wanted by the provider
   * @param vehicleType {@link VehicleType} of the offered {@link Vehicle}
   * @param make brand of the offered {@link Vehicle}
   * @param model model of the offered {@link Vehicle}
   * @param year in which the offered {@link Vehicle} was produced
   * @param length of the vehicle in cm
   * @param width of the vehicle in cm
   * @param height of the vehicle in cm
   * @param fuelType Rather specifies the vehicle's fuel type
   * @param transmission {@link swtcamper.backend.entities.TransmissionType} of the offered vehicle
   * @param seats Amount of seats that the offered vehicle has
   * @param beds Amount of beds that the offered vehicle has
   * @param roofTent Does the vehicle have a roof tent?
   * @param roofRack Does the vehicle have a roof rack?
   * @param bikeRack Does the vehicle have a bike rack?
   * @param shower Does the vehicle have a shower?
   * @param toilet Does the vehicle have a toilet?
   * @param kitchenUnit Does the vehicle have a kitchen unit?
   * @param fridge Does the vehicle have a fridge?
   * @return {@link OfferDTO} of the new offer
   */
  public OfferDTO update(
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
    boolean fridge
  ) throws GenericServiceException {
    return modelMapper.offerToOfferDTO(
      offerService.update(
        offerId,
        creator,
        offeredObject,
        //Offer-Parameter
        title,
        location,
        contact,
        particularities,
        bookings,
        price,
        active,
        rentalConditions,
        blockedDates,
        //VehicleFeatures-Parameter
        vehicleType,
        make,
        model,
        year,
        length,
        width,
        height,
        fuelType,
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
        modelMapper.userToUserDTO(userController.getLoggedInUser())
      )
    );
  }

  /**
   * Deletes an existing offer from the database
   * @param id ID of the offer to delete
   * @throws GenericServiceException if there is no offer with the given ID
   */
  public void delete(long id) throws GenericServiceException {
    try {
      offerService.delete(
        id,
        modelMapper.userToUserDTO(userController.getLoggedInUser())
      );
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }

  /**
   * Gets all offers from the database that were created by a user
   * @param user {@link User} whose offers shall be searched
   * @return List of OfferDTOs of offers that were created by the user
   * @throws GenericServiceException
   */
  public List<OfferDTO> getOffersCreatedByUser(User user)
    throws GenericServiceException {
    return modelMapper.offersToOfferDTOs(
      offerService.getOffersCreatedByUser(user)
    );
  }

  /**
   * Looks for fitting offers in the database
   *
   * @param filter {@link Filter} to hold search settings.
   * @return (Array)List of offers that fit to the given Filter
   * @throws GenericServiceException
   */
  public List<OfferDTO> getFilteredOffers(Filter filter)
    throws GenericServiceException {
    return offerService.getFilteredOffers(filter);
  }

  /**
   * Promotes an offer, s.t. it is highlighted next to the normal offers
   * @param offerID ID of the offer to promote
   * @throws GenericServiceException
   */
  public OfferDTO promoteOffer(long offerID) throws GenericServiceException {
    return modelMapper.offerToOfferDTO(offerService.promoteOffer(offerID));
  }

  /**
   * Degrades an offer, s.t. it is just seen like any other offer
   * @param offerID ID of the offer to degrade
   * @throws GenericServiceException
   */
  public OfferDTO degradeOffer(long offerID) throws GenericServiceException {
    return modelMapper.offerToOfferDTO(offerService.degradeOffer(offerID));
  }
}
