package xtasks.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import xtasks.api.ModelMapper;
import xtasks.api.contract.IOfferController;
import xtasks.api.contract.OfferDTO;
import xtasks.backend.services.OfferService;

public class OfferController implements IOfferController {

    @Autowired
    OfferService offerService;

    @Autowired
    ModelMapper modelMapper;

    public OfferDTO create(
            // TODO throws Exception
            // TODO Parameter: alle Daten die der ViewController an den OfferController weitergibt
        ) {
        return modelMapper.offerToOfferDTO(offerService.create(
                // TODO Parameter übergeben
        ));
    }
}
