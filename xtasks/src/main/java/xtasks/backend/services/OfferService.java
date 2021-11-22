package xtasks.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xtasks.backend.entities.Offer;
import xtasks.backend.entities.Vehicle;
import xtasks.backend.entities.VehicleFeatures;
import xtasks.backend.entities.VehicleOffer;
import xtasks.backend.repositories.VehicleRepository;

@Service
public class OfferService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleFeaturesRepository vehicleFeaturesRepository;

    @Autowired
    private OfferRepository offerRepository;

    public Offer create(
            // TODO eintragen:
            //  alle Offer-Attribute
            //  alle Vehicle-Attribute
            //  alle VehicleFeatures-Attribute
        ) {

        // !!! Hier werden die ganzen zusammenhängenden Objekte erstellt und die IDs untereinander verteilt !!!

        VehicleOffer vehicleOffer = new VehicleOffer();
        // TODO set Offer-Attribute

        Vehicle vehicle = new Vehicle();
        // TODO set Vehicle-Attribute
        vehicle.setOfferID(vehicleOffer.getID());

        VehicleFeatures vehicleFeatures = new VehicleFeatures();
        // TODO set VehicleFeatures-Attribute
        vehicleFeatures.setVehicleID(vehicle.getID());

        vehicle.setVehicleFeaturesID(vehicleFeatures.getID());

        vehicleOffer.setVehicleID(vehicle.getID());

        // Objekte erstellt und IDs verteilt --> ab in die DB

        vehicleFeaturesRepository.save(vehicleFeatures);
        vehicleRepository.save(vehicle);
        return OfferRepository.save(vehicleOffer);
    }

}
