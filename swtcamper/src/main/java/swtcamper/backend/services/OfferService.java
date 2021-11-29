package swtcamper.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleFeatures;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;

@Service
public class OfferService {

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private VehicleFeaturesRepository vehicleFeaturesRepository;

  @Autowired
  private OfferRepository offerRepository;

  public Offer create(
    // TODO validation

    // Offer-Parameter
    Long price,
    String rentalConditions,

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
    // !!! Hier werden die ganzen zusammenhängenden Objekte erstellt und die IDs untereinander verteilt !!!

    Vehicle vehicle = new Vehicle();
    vehicle.setPictureURLs(pictureURLs);
    vehicle.setParticularities(particularities);

    VehicleFeatures vehicleFeatures = new VehicleFeatures(vehicle);
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

    vehicle.setVehicleFeatures(vehicleFeatures);

    Offer offer = new Offer(vehicle, price, rentalConditions);

    // Objekte erstellt und IDs verteilt --> ab in die DB

    vehicleFeaturesRepository.save(vehicleFeatures);
    vehicleRepository.save(vehicle);
    return offerRepository.save(offer);
  }


}
