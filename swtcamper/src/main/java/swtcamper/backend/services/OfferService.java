package xtasks.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xtasks.backend.entities.Offer;
import xtasks.backend.entities.Vehicle;
import xtasks.backend.entities.VehicleFeatures;
import xtasks.backend.repositories.OfferRepository;
import xtasks.backend.repositories.VehicleFeaturesRepository;
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

        Vehicle vehicle = new Vehicle();
        // TODO set Vehicle-Attribute

        VehicleFeatures vehicleFeatures = new VehicleFeatures(vehicle);
        // TODO set VehicleFeatures-Attribute

        vehicle.setVehicleFeatures(vehicleFeatures);

        Offer offer = new Offer(vehicle);
        // TODO set Offer-Attribute

        // Objekte erstellt und IDs verteilt --> ab in die DB

        vehicleFeaturesRepository.save(vehicleFeatures);
        vehicleRepository.save(vehicle);
        return offerRepository.save(offer);
    }

}
