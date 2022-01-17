package swtcamper.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IOfferController;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;
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
    //Vehicle-Parameter
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
        //Vehicle-Parameter
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
   *
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
   *
   * @param user {@link User} whose offers shall be searched
   * @return List of OfferDTOs of offers that were created by the user
   * @throws GenericServiceException
   */
  public List<OfferDTO> getOffersCreatedByUser(User user)
    throws GenericServiceException {
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
  public List<OfferDTO> getFilteredOffers(Filter filter)
    throws GenericServiceException {
    return filter.isEmpty()
      ? offers()
      : offers()
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
              .getVehicleFeatures()
              .getVehicleType()
              .equals(filter.getVehicleType())
          ) &&
          (
            filter.getVehicleBrand() == null ||
            offerDTO
              .getOfferedObject()
              .getVehicleFeatures()
              .getMake()
              .toLowerCase()
              .contains(filter.getVehicleBrand().toLowerCase())
          ) &&
          (
            filter.getConstructionYear() == 0 ||
            Integer.parseInt(
              offerDTO.getOfferedObject().getVehicleFeatures().getYear()
            ) >=
            filter.getConstructionYear()
          ) &&
          (
            filter.getMaxPricePerDay() == 0 ||
            offerDTO.getPrice() <= filter.getMaxPricePerDay()
          ) &&
          (
            filter.getFuelType() == null ||
            offerDTO
              .getOfferedObject()
              .getVehicleFeatures()
              .getFuelType()
              .equals(filter.getFuelType())
          ) &&
          (
            filter.getTransmissionType() == null ||
            offerDTO
              .getOfferedObject()
              .getVehicleFeatures()
              .getTransmission()
              .toUpperCase()
              .equals(filter.getTransmissionType().toString())
          ) &&
          (
            filter.getSeatAmount() == 0 ||
            offerDTO.getOfferedObject().getVehicleFeatures().getSeats() >=
            filter.getSeatAmount()
          ) &&
          (
            filter.getBedAmount() == 0 ||
            offerDTO.getOfferedObject().getVehicleFeatures().getBeds() >=
            filter.getBedAmount()
          ) &&
          evalCheckBoxes(offerDTO, filter)
        )
        .collect(Collectors.toList());
  }

  /**
   * Evaluates whether one of the checkboxes in the offer does not equal to its value in the filter
   * @param offerDTO Offer that shall be looked in
   * @param filter {@link Filter} that holds the settings of the checkboxes
   * @return true if all checkboxes in the offer equal to their values in the filter, false if there is at least one that does not
   */
  private boolean evalCheckBoxes(OfferDTO offerDTO, Filter filter) {
    List<Boolean> booleanList = new ArrayList<>();

    if (filter.isRoofTent()) booleanList.add(
      offerDTO.getOfferedObject().getVehicleFeatures().isRoofTent()
    );
    if (filter.isRoofRack()) booleanList.add(
      offerDTO.getOfferedObject().getVehicleFeatures().isRoofRack()
    );
    if (filter.isBikeRack()) booleanList.add(
      offerDTO.getOfferedObject().getVehicleFeatures().isBikeRack()
    );
    if (filter.isShower()) booleanList.add(
      offerDTO.getOfferedObject().getVehicleFeatures().isShower()
    );
    if (filter.isToilet()) booleanList.add(
      offerDTO.getOfferedObject().getVehicleFeatures().isToilet()
    );
    if (filter.isKitchen()) booleanList.add(
      offerDTO.getOfferedObject().getVehicleFeatures().isKitchenUnit()
    );
    if (filter.isFridge()) booleanList.add(
      offerDTO.getOfferedObject().getVehicleFeatures().isFridge()
    );
    return !booleanList.contains(false);
  }
}
