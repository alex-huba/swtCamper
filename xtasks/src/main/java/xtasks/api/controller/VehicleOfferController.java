package xtasks.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import xtasks.api.ModelMapper;
import xtasks.api.contract.IVehicleOfferController;
import xtasks.backend.entities.Availability;
import xtasks.backend.services.VehicleService;

public class VehicleOfferController implements IVehicleOfferController {

    @Autowired
    VehicleOfferService vehicleOfferService;

    @Autowired
    ModelMapper modelMapper;

    public VehicleOfferDTO create(
            Availability availability,
            Long price,
            String rentalStartDate,
            String rentalReturnDate
    ) {

    }
}
