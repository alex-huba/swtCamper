package xtasks.api.contract;

public interface IVehicleController {

    public VehicleDTO create(
            Long vehicleID,
            VehicleTypeDTO vehicleType,
            VehicleFeaturesDTO vehicleFeatures,
            String[] pictureURLs,
            String[] particularities
        );

}
