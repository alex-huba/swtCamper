package swtcamper.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.entities.*;
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
  private BookingController bookingController;

  public Offer create(
    // TODO validation
    User creator,
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

    Offer offer = new Offer(
      creator,
      vehicle,
      title,
      location,
      contact,
      particularities,
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
  ) throws GenericServiceException {
    Optional<Offer> offerResponse = offerRepository.findById(offerId);
    Offer offer;
    if (offerResponse.isPresent()) {
      offer = offerResponse.get();
    } else {
      throw new GenericServiceException(
        "There is no offer with specified ID " + offerId
      );
    }

    // check if offer is in rent right now
    for (Booking booking : bookingController.getAllBookings()) {
      if (booking.getOffer().getOfferID() == offerId) {
        throw new GenericServiceException(
          "Cannot modify offer with ID " +
          offerId +
          " while it is part of an active booking."
        );
      }
    }

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
    vehicleFeaturesRepository.save(vehicleFeatures);

    vehicle.setVehicleFeatures(vehicleFeatures);
    vehicle.setPictureURLs(pictureURLs);
    vehicleRepository.save(vehicle);

    offer.setCreator(creator);
    offer.setOfferedObject(vehicle);
    offer.setBookings(bookings);
    offer.setTitle(title);
    offer.setLocation(location);
    offer.setContact(contact);
    offer.setParticularities(particularities);
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
    // check if offer is in rent right now
    for (Booking booking : bookingController.getAllBookings()) {
      if (booking.getOffer().getOfferID() == id) {
        throw new GenericServiceException(
          "Cannot modify offer with ID " +
          id +
          " while it is part of an active booking."
        );
      }
    }
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
