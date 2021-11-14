package xtasks.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import xtasks.api.ModelMapper;
import xtasks.api.contract.IVehicleController;
import xtasks.api.contract.VehicleDTO;
import xtasks.api.contract.VehicleFeaturesDTO;
import xtasks.api.contract.VehicleTypeDTO;
import xtasks.backend.services.VehicleService;

public class VehicleController implements IVehicleController {

    @Autowired
    VehicleService vehicleService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public VehicleDTO create(
            Long vehicleID,
            VehicleTypeDTO vehicleType,
            VehicleFeaturesDTO vehicleFeatures,
            String[] pictureURLs,
            String[] particularities
    ) {
       return modelMapper.vehicleToVehicleDTO(vehicleService.create(
               vehicleID,
               vehicleType,
               vehicleFeatures,
               pictureURLs,
               particularities
       ));
    }
}
