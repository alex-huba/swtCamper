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
  private UserController userController;

  @Override
  public List<OfferDTO> offers() throws GenericServiceException {
    return modelMapper.offersToOfferDTOs(offerService.offers());
  }

  @Override
  public Offer getOfferById(long id) throws GenericServiceException {
    return offerService.getOfferById(id);
  }

  @Override
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

  @Override
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

  @Override
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

  @Override
  public List<OfferDTO> getOffersCreatedByUser(User user)
    throws GenericServiceException {
    return modelMapper.offersToOfferDTOs(
      offerService.getOffersCreatedByUser(user)
    );
  }

  @Override
  public List<OfferDTO> getFilteredOffers(Filter filter)
    throws GenericServiceException {
    return offerService.getFilteredOffers(filter);
  }

  @Override
  public OfferDTO promoteOffer(long offerID) throws GenericServiceException {
    return modelMapper.offerToOfferDTO(offerService.promoteOffer(offerID));
  }

  @Override
  public OfferDTO degradeOffer(long offerID) throws GenericServiceException {
    return modelMapper.offerToOfferDTO(offerService.degradeOffer(offerID));
  }
}
