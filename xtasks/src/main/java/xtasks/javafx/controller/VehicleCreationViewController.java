package xtasks.javafx.controller;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import xtasks.api.contract.VehicleDTO;
import xtasks.api.controller.VehicleController;
import xtasks.backend.entities.VehicleFeatures;
import xtasks.backend.entities.VehicleType;

public class VehicleCreationViewController {

    @Autowired
    private VehicleController vehicleController;

    @FXML
    public void createNewVehicle() {

        // Vehicle features lesen und zu VehicleFeatures-Objekt machen,
        // dann an VehicleController.create() weitergeben
        // (mit den anderen Vehicle-Daten)

        VehicleDTO vehicleDTO = vehicleController.create(
                vehicleType,
                vehicleFeatures,
                pictureURLs,
                particularities
        );
    }

}
