package xtasks.api.contract;

import xtasks.backend.entities.Vehicle;

public class VehicleFeaturesDTO {

    Vehicle vehicle;

    // Hersteller
    private String make;

    // Fahrzeugtyp
    private String type;
    private String model;
    private String year;

    private double length;
    private double width;
    private double height;

    private String engine;

    //Getriebe (manuell oder automatisch)
    private String transmission;

}
