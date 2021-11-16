package xtasks.api.contract;

import xtasks.backend.entities.VehicleFeatures;
import xtasks.backend.entities.VehicleType;

public interface IVehicleController {

    // TODO javadoc

    public VehicleDTO create(
            Long vehicleID,
            VehicleType vehicleType,
            VehicleFeatures vehicleFeatures,
            String[] pictureURLs,
            String[] particularities
        );

}
