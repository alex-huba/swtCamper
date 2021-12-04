package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IOfferController;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;

public class OfferController implements IOfferController {

    @Autowired
    OfferService offerService;

    @Autowired
    ModelMapper modelMapper;

    public List<OfferDTO> offers() throws GenericServiceException {
        return modelMapper.offersToOfferDTOs(offerService.offers());
    }

    public OfferDTO create(
            // Offer-Parameter
            Long price,
            String rentalStartDate,
            String rentalReturnDate,
            //Vehicle-Parameter
            String[] pictureURLs,
            String[] particularities,
            //VehicleFeatures-Parameter
            VehicleType vehicleType,
            String make,
            String model,
            String year,
            double length,
            double width,
            double height,
            String engine,
            String transmission,
            int seats,
            int beds,
            boolean roofTent,
            boolean roofRack,
            boolean bikeRack,
            boolean shower,
            boolean toilet,
            boolean kitchenUnit,
            boolean fridge
    ) { // TODO throws Exception
        return modelMapper.offerToOfferDTO(
                offerService.create(
                        //Offer-Parameter
                        price,
                        rentalStartDate,
                        rentalReturnDate,
                        //Vehicle-Parameter
                        pictureURLs,
                        particularities,
                        //VehicleFeatures-Parameter
                        vehicleType,
                        make,
                        model,
                        year,
                        length,
                        width,
                        height,
                        engine,
                        transmission,
                        seats,
                        beds,
                        roofTent,
                        roofRack,
                        bikeRack,
                        shower,
                        toilet,
                        kitchenUnit,
                        fridge
                )
        );
    }

    @Override
    public List<OfferDTO> getFilteredOffers(Filter filter) throws GenericServiceException {
        return offers().stream().filter(offerDTO ->
                offerDTO.getLocation == filter.getLocation()
                        && offerDTO.getAvailableFrom().isBefore(filter.getPickUpDate())
                        && offerDTO.getAvailableUntil().isAfter(filter.getReturnDate())
                        && offerDTO.getVehicleType().equals(filter.getVehicleType())
                        && offerDTO.getVehicleBrand().equals(filter.getVehicleBrand())
                        && offerDTO.getConstructionYear() >= filter.getConstructionYear()
                        && offerDTO.getPricePerDay() <= filter.getMaxPricePerDay()
                        && offerDTO.getEngine().equals(filter.getEngine())
                        && offerDTO.getTransmissionType.equals(filter.getTransmissionType())
                        && offerDTO.getSeatAmount() >= filter.getSeatAmount()
                        && offerDTO.getBedAmount() >= filter.getBedAmount()
                        && offerDTO.isRoofTent() == filter.isRoofTent()
                        && offerDTO.isRoofRack() == filter.isRoofRack()
                        && offerDTO.isBikeRack() == filter.isBikeRack()
                        && offerDTO.isShower() == filter.isShower()
                        && offerDTO.isToilet() == filter.isToilet()
                        && offerDTO.isKitchen() == filter.isKitchen()
                        && offerDTO.isFridge() == filter.isFridge()
        );
    }
}
