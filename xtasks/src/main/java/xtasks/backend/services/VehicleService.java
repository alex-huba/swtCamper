package xtasks.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        vehicle.setVehicleID(vehicleID);
        vehicle.setVehicleType(vehicleType);
        vehicle.setVehicleFeaturesID(vehicleFeatures.getID());
        vehicle.setPictureURLs(pictureURLs);
        vehicle.setParticularities(particularities);

        // TODO implement vehicleFeaturesRepository
        vehicleFeaturesRepository.save(vehicleFeatures);
        return vehicleRepository.save(vehicle);
    }
}
