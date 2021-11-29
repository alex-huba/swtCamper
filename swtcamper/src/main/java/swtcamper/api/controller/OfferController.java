package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IOfferController;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.OfferService;

public class OfferController implements IOfferController {

  @Autowired
  OfferService offerService;

  @Autowired
  ModelMapper modelMapper;

  public OfferDTO create(
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
  ) { // TODO throws Exception
    return modelMapper.offerToOfferDTO(
      offerService.create(

        //Offer-Parameter
        price,
        rentalConditions,

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
}
