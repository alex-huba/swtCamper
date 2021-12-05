package swtcamper.api.controller;

import java.util.ArrayList;
import java.util.List;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IOfferController;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleFeatures;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
    long price,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash,
    //Vehicle-Parameter
    String[] pictureURLs,
    String[] particularities,
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
  ) { // TODO throws Exception
    return modelMapper.offerToOfferDTO(
      offerService.create(
        //Offer-Parameter
        price,
        minAge25,
        borderCrossingAllowed,
        depositInCash,
        //Vehicle-Parameter
        pictureURLs,
        particularities,
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
    ArrayList<Long> bookings,
    long price,
    boolean active,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash,
    //Vehicle-Parameter
    String[] pictureURLs,
    String[] particularities,
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
        bookings,
        price,
        active,
        minAge25,
        borderCrossingAllowed,
        depositInCash,
        //Vehicle-Parameter
        pictureURLs,
        particularities,
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
      // TODO was passiert mit dem Vehicle des Offers
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }

  @Override
    public List<OfferDTO> getFilteredOffers(Filter filter) throws GenericServiceException {
        return offers().stream().filter(offerDTO ->
//                offerDTO.getLocation() == filter.getLocation()
//                        && offerDTO.getAvailableFrom().isBefore(filter.getPickUpDate())
//                        && offerDTO.getAvailableUntil().isAfter(filter.getReturnDate())
//                        &&
                        offerDTO.getOfferedObject().getVehicleFeatures().getVehicleType().equals(filter.getVehicleType())
                        && offerDTO.getOfferedObject().getVehicleFeatures().getMake().equals(filter.getVehicleBrand())
                        && Integer.parseInt(offerDTO.getOfferedObject().getVehicleFeatures().getYear()) >= filter.getConstructionYear()
                        && offerDTO.getPrice() <= filter.getMaxPricePerDay()
                        && offerDTO.getOfferedObject().getVehicleFeatures().getEngine().equals(filter.getEngine())
                        && offerDTO.getOfferedObject().getVehicleFeatures().getTransmission().toUpperCase().equals(filter.getTransmissionType().toString())
                        && offerDTO.getOfferedObject().getVehicleFeatures().getSeats() >= filter.getSeatAmount()
                        && offerDTO.getOfferedObject().getVehicleFeatures().getBeds() >= filter.getBedAmount()
                        && offerDTO.getOfferedObject().getVehicleFeatures().isRoofTent() == filter.isRoofTent()
                        && offerDTO.getOfferedObject().getVehicleFeatures().isRoofRack() == filter.isRoofRack()
                        && offerDTO.getOfferedObject().getVehicleFeatures().isBikeRack() == filter.isBikeRack()
                        && offerDTO.getOfferedObject().getVehicleFeatures().isShower() == filter.isShower()
                        && offerDTO.getOfferedObject().getVehicleFeatures().isToilet() == filter.isToilet()
                        && offerDTO.getOfferedObject().getVehicleFeatures().isKitchenUnit() == filter.isKitchen()
                        && offerDTO.getOfferedObject().getVehicleFeatures().isFridge() == filter.isFridge()
        ).collect(Collectors.toList());
    }
}
