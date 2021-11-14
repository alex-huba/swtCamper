package xtasks.backend.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;

public class Vehicle {

    Availability availability;

    @Id
    @GeneratedValue
    private Long vehicleID;

    private VehicleType vehicleType;
    private VehicleFeatures vehicleFeatures;

    private String[] pictureURLs;
    private String[] particularities;


    public Vehicle(VehicleType vehicleType){
        this.availability = Availability.AVAILABLE;
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "Vehicle: " +
                "vehicleStatus=" + availability +
                ", vehicleID=" + vehicleID +
                ", vehicleType=" + vehicleType +
                ", pictureURLs=" + Arrays.toString(pictureURLs) +
                ", particularities='" + particularities + '\'';
    }

    public Availability getVehicleStatus() {
        return availability;
    }

    public void setVehicleStatus(Availability availability) {
        this.availability = availability;
    }

    public Long getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Long vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String[] getPictureURLs() {
        return pictureURLs;
    }

    public void setPictureURLs(String[] pictureURLs) {
        this.pictureURLs = pictureURLs;
    }

    public String[] getParticularities() {
        return particularities;
    }

    public void setParticularities(String[] particularities) {
        this.particularities = particularities;
    }
}
