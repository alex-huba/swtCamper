package xtasks.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import xtasks.api.ModelMapper;
import xtasks.api.contract.IVehicleController;
import xtasks.api.contract.VehicleDTO;
import xtasks.backend.entities.VehicleFeatures;
import xtasks.backend.entities.VehicleType;
import xtasks.backend.services.VehicleService;

public class VehicleController implements IVehicleController {

    @Autowired
    VehicleService vehicleService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public VehicleDTO create(
            Long vehicleID,
            VehicleType vehicleType,
            // VehicleFeatures unverpackt: Dusche ja nein, etc
            String[] pictureURLs,
            String[] particularities
    ) {
        VehicleFeatures vehicleFeatures = new VehicleFeatures(
                //Attribute:
                //this.Dusche = true;
        )

        return modelMapper.vehicleToVehicleDTO(vehicleService.create(
               vehicleID,
               vehicleType,
               vehicleFeatures,
               pictureURLs,
               particularities
       ));
    }
}
