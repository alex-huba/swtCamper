package swtcamper.api.contract.interfaces;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.*;
import swtcamper.backend.services.exceptions.GenericServiceException;

public interface IOfferController {
  List<OfferDTO> offers() throws GenericServiceException;

  Offer getOfferById(long id) throws GenericServiceException;

  OfferDTO create(
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
  ) throws GenericServiceException;

  OfferDTO update(
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
  ) throws GenericServiceException;

  void delete(long id) throws GenericServiceException;

  List<OfferDTO> getOffersCreatedByUser(User user)
    throws GenericServiceException;

  List<OfferDTO> getFilteredOffers(Filter filter)
    throws GenericServiceException;

  OfferDTO promoteOffer(long offerID) throws GenericServiceException;

  OfferDTO degradeOffer(long offerID) throws GenericServiceException;
}
