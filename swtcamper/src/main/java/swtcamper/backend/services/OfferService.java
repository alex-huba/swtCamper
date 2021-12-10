package swtcamper.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleFeatures;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Service
public class OfferService {

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private VehicleFeaturesRepository vehicleFeaturesRepository;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  OfferService offerService;

  public Offer create(
    // TODO validation

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
  ) {
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
    vehicle.setPictureURLs(pictureURLs);
    vehicle.setParticularities(particularities);

    Offer offer = new Offer(
      vehicle,
      price,
      minAge25,
      borderCrossingAllowed,
      depositInCash
    );
    vehicleRepository.save(vehicle);
    return offerRepository.save(offer);
  }

  public Offer update(
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
  ) {
    Optional<Offer> offerResponse = offerRepository.findById(offerId);
    Offer offer = offerResponse.get();

    Optional<Vehicle> vehicleResponse = vehicleRepository.findById(
      offeredObject.getVehicleID()
    );
    Vehicle vehicle = vehicleResponse.get();

    Optional<VehicleFeatures> vehicleFeaturesResponse = vehicleFeaturesRepository.findById(
      vehicle.getVehicleFeatures().getVehicleFeaturesID()
    );
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

    vehicle.setVehicleFeatures(vehicleFeatures);
    vehicle.setPictureURLs(pictureURLs);
    vehicle.setParticularities(particularities);

    vehicleRepository.save(vehicle);

    offer.setOfferedObject(vehicle);
    offer.setBookings(bookings);
    offer.setPrice(price);
    offer.setActive(active);
    offer.setMinAge25(minAge25);
    offer.setBorderCrossingAllowed(borderCrossingAllowed);
    offer.setDepositInCash(depositInCash);

    return offerRepository.save(offer);
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
  public void delete(long id) throws GenericServiceException {
    try {
      offerRepository.deleteById(id);
    } catch (IllegalArgumentException e) {
      throw new GenericServiceException("The passed ID is not available: " + e);
    }
  }

  public List<Offer> offers() {
    return offerRepository.findAll();
  }
}
