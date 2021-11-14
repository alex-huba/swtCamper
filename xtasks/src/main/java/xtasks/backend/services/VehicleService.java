package xtasks.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xtasks.api.contract.VehicleFeaturesDTO;
import xtasks.api.contract.VehicleTypeDTO;
import xtasks.backend.entities.Vehicle;
import xtasks.backend.entities.VehicleFeatures;
import xtasks.backend.entities.VehicleType;
import xtasks.backend.repositories.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle create(
            Long vehicleID,
            VehicleType vehicleType,
            VehicleFeatures vehicleFeatures,
            String[] pictureURLs,
            String[] particularities
    ) {
        // TODO throws Exception
        // TODO validate Methoden
        Vehicle vehicle = new Vehicle();
        // TODO Melissa: Leeren Vehicle Konstruktor mit super() anlegen
        vehicle.setVehicleID(vehicleID);
        vehicle.setVehicleType(vehicleType);
        vehicle.setVehicleFeatures(vehicleFeatures);
        vehicle.setPictureURLs(pictureURLs);
        vehicle.setParticularities(particularities);

        return vehicleRepository.save(vehicle);
    }
}
