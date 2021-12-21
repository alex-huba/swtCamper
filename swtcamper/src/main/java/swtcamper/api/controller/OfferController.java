package swtcamper.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IOfferController;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;
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
  VehicleFeaturesRepository vehicleFeaturesRepository;

  @Override
  public List<OfferDTO> offers() throws GenericServiceException {
    return modelMapper.offersToOfferDTOs(offerService.offers());
  }

  public OfferDTO create(
    // Offer-Parameter
    String title,
    String location,
    String contact,
    String description,
    long price,
    ArrayList<String> rentalConditions,
    //Vehicle-Parameter
    String[] pictureURLs,
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
  ) {
    return modelMapper.offerToOfferDTO(
      offerService.create(
        //Offer-Parameter
        title,
        location,
        contact,
        description,
        price,
        rentalConditions,
        //Vehicle-Parameter
        pictureURLs,
        //VehicleFeatures-Parameter
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
      )
    );
  }

  public OfferDTO update(
    long offerId,
    Vehicle offeredObject,
    // Offer-Parameter
    String title,
    String location,
    String contact,
    String description,
    ArrayList<Long> bookings,
    long price,
    boolean active,
    ArrayList<String> rentalConditions,
    //Vehicle-Parameter
    String[] pictureURLs,
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
    return modelMapper.offerToOfferDTO(
      offerService.update(
        offerId,
        offeredObject,
        //Offer-Parameter
        title,
        location,
        contact,
        description,
        bookings,
        price,
        active,
        rentalConditions,
        //Vehicle-Parameter
        pictureURLs,
        //VehicleFeatures-Parameter
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
      )
    );
  }

  public void delete(long id) throws GenericServiceException {
    try {
      offerService.delete(id);
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }

  /**
   * Looks for fitting offers in the database.
   * @param filter Filter to hold search settings.
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
            filter.getLocation() != null
              ? offerDTO.getLocation().equals(filter.getLocation())
              : true
          ) &&
          (
            filter.getVehicleType() != null
              ? offerDTO
                .getOfferedObject()
                .getVehicleFeatures()
                .getVehicleType()
                .equals(filter.getVehicleType())
              : true
          ) &&
          (
            filter.getVehicleBrand() != null
              ? offerDTO
                .getOfferedObject()
                .getVehicleFeatures()
                .getMake()
                .equals(filter.getVehicleBrand())
              : true
          ) &&
          (
            filter.getConstructionYear() != 0
              ? Integer.parseInt(
                offerDTO.getOfferedObject().getVehicleFeatures().getYear()
              ) >=
              filter.getConstructionYear()
              : true
          ) &&
          (
            filter.getMaxPricePerDay() != 0
              ? offerDTO.getPrice() <= filter.getMaxPricePerDay()
              : true
          ) &&
          (
            filter.getEngine() != null
              ? offerDTO
                .getOfferedObject()
                .getVehicleFeatures()
                .getEngine()
                .equals(filter.getEngine())
              : true
          ) &&
          (
            filter.getTransmissionType() != null
              ? offerDTO
                .getOfferedObject()
                .getVehicleFeatures()
                .getTransmission()
                .toUpperCase()
                .equals(filter.getTransmissionType().toString())
              : true
          ) &&
          (
            filter.getSeatAmount() != 0
              ? offerDTO.getOfferedObject().getVehicleFeatures().getSeats() >=
              filter.getSeatAmount()
              : true
          ) &&
          (
            filter.getBedAmount() != 0
              ? offerDTO.getOfferedObject().getVehicleFeatures().getBeds() >=
              filter.getBedAmount()
              : true
          ) &&
          (filter.isExcludeInactive() ? offerDTO.isActive() : true) &&
          evalCheckBoxes(offerDTO, filter)
        )
        .collect(Collectors.toList());
  }

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
