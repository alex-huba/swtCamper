package swtcamper.api.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.GenericServiceException;

public interface IOfferController {
  List<OfferDTO> offers() throws GenericServiceException;

  OfferDTO create(
    // Offer-Parameter
    String title,
    String location,
    String contact,
    String particularities,
    long price,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash,
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
  );

  OfferDTO update(
    long offerId,
    Vehicle offeredObject,
    // Offer-Parameter
    String title,
    String location,
    String contact,
    String particularities,
    ArrayList<Long> bookings,
    long price,
    boolean active,
    boolean minAge25,
    boolean borderCrossingAllowed,
    boolean depositInCash,
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
  ) throws GenericServiceException;

  void delete(long id) throws GenericServiceException;

  List<OfferDTO> getFilteredOffers(Filter filter)
    throws GenericServiceException;
}
