package xtasks.api.contract;

import xtasks.backend.entities.Availability;

public interface IVehicleOfferController {

    // TODO javadoc

    public VehicleOfferDTO create(
            Availability availability,
            Long price,
            String rentalStartDate,
            String rentalReturnDate
    );

}
