package swtcamper.api.contract.interfaces;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.*;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;

public interface IOfferController {

  /**
   * Get a List of OfferDTOs of all available offers in the database
   * @return List of OfferDTOs
   * @throws GenericServiceException
   */
  List<OfferDTO> offers() throws GenericServiceException;

  /**
   * Get a specific offer by its ID
   * @param id ID of the wanted offer
   * @return wanted Offer
   * @throws GenericServiceException
   */
  OfferDTO getOfferById(long id) throws GenericServiceException;

  /**
   * Creates a new offer and forwards it to the {@link OfferService} where it gets saved to the database
   * @param title of the new offer
   * @param location where the {@link Vehicle} can be picked up from
   * @param contact How the provider can be reached
   * @param particularities Any points that should be said about the offer
   * @param price per day for the vehicle
   * @param rentalConditions List of (String) conditions that are wanted by the provider
   * @param vehicleType {@link VehicleType} of the offered {@link Vehicle}
   * @param make brand of the offered {@link Vehicle}
   * @param model model of the offered {@link Vehicle}
   * @param year in which the offered {@link Vehicle} was produced
   * @param length of the vehicle in cm
   * @param width of the vehicle in cm
   * @param height of the vehicle in cm
   * @param fuelType Rather specifies the vehicle's fuel type
   * @param transmission {@link swtcamper.backend.entities.TransmissionType} of the offered vehicle
   * @param seats Amount of seats that the offered vehicle has
   * @param beds Amount of beds that the offered vehicle has
   * @param roofTent Does the vehicle have a roof tent?
   * @param roofRack Does the vehicle have a roof rack?
   * @param bikeRack Does the vehicle have a bike rack?
   * @param shower Does the vehicle have a shower?
   * @param toilet Does the vehicle have a toilet?
   * @param kitchenUnit Does the vehicle have a kitchen unit?
   * @param fridge Does the vehicle have a fridge?
   * @return {@link OfferDTO} of the new offer
   */
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

  /**
   * Updates an existing offer and forwards it to the {@link OfferService} where it gets saved to the database
   * @param title of the new offer
   * @param location where the {@link Vehicle} can be picked up from
   * @param contact How the provider can be reached
   * @param particularities Any points that should be said about the offer
   * @param price per day for the vehicle
   * @param rentalConditions List of (String) conditions that are wanted by the provider
   * @param vehicleType {@link VehicleType} of the offered {@link Vehicle}
   * @param make brand of the offered {@link Vehicle}
   * @param model model of the offered {@link Vehicle}
   * @param year in which the offered {@link Vehicle} was produced
   * @param length of the vehicle in cm
   * @param width of the vehicle in cm
   * @param height of the vehicle in cm
   * @param fuelType Rather specifies the vehicle's fuel type
   * @param transmission {@link swtcamper.backend.entities.TransmissionType} of the offered vehicle
   * @param seats Amount of seats that the offered vehicle has
   * @param beds Amount of beds that the offered vehicle has
   * @param roofTent Does the vehicle have a roof tent?
   * @param roofRack Does the vehicle have a roof rack?
   * @param bikeRack Does the vehicle have a bike rack?
   * @param shower Does the vehicle have a shower?
   * @param toilet Does the vehicle have a toilet?
   * @param kitchenUnit Does the vehicle have a kitchen unit?
   * @param fridge Does the vehicle have a fridge?
   * @return {@link OfferDTO} of the new offer
   */
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

  /**
   * Deletes an existing offer from the database
   * @param id ID of the offer to delete
   * @throws GenericServiceException if there is no offer with the given ID
   */
  void delete(long id) throws GenericServiceException;

  /**
   * Gets all offers from the database that were created by a user
   * @param user {@link User} whose offers shall be searched
   * @return List of OfferDTOs of offers that were created by the user
   * @throws GenericServiceException
   */
  List<OfferDTO> getOffersCreatedByUser(User user)
    throws GenericServiceException;

  /**
   * Looks for fitting offers in the database
   *
   * @param filter {@link Filter} to hold search settings.
   * @return (Array)List of offers that fit to the given Filter
   * @throws GenericServiceException
   */
  List<OfferDTO> getFilteredOffers(Filter filter)
    throws GenericServiceException;

  /**
   * Promotes an offer, s.t. it is highlighted next to the normal offers
   * @param offerID ID of the offer to promote
   * @throws GenericServiceException
   */
  OfferDTO promoteOffer(long offerID) throws GenericServiceException;

  /**
   * Degrades an offer, s.t. it is just seen like any other offer
   * @param offerID ID of the offer to degrade
   * @throws GenericServiceException
   */
  OfferDTO degradeOffer(long offerID) throws GenericServiceException;
}
