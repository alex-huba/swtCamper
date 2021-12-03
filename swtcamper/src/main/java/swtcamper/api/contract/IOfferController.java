package swtcamper.api.contract;

import org.springframework.stereotype.Component;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.ArrayList;
import java.util.List;

public interface IOfferController {
  OfferDTO create(
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
  ); // TODO throws Exception

  OfferDTO update(
          long offerId,
          long offeredObjectId,

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
  ) throws GenericServiceException;

  void delete(long id) throws GenericServiceException;

  List<OfferDTO> offers() throws GenericServiceException;
}
