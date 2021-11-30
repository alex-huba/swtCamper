package swtcamper.api.contract;

import swtcamper.backend.entities.VehicleType;

public interface IOfferController {
  OfferDTO create(
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
  ); // TODO throws Exception
}
