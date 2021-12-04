package swtcamper.api.contract;

import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;

public interface IOfferController {
  OfferDTO create(
    // Offer-Parameter
    Long price,
    String rentalStartDate,
    String rentalReturnDate,
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

    List<OfferDTO> getFilteredOffers(Filter filter) throws GenericServiceException;
}
