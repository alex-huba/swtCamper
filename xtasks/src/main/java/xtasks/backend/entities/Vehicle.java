package xtasks.backend.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;

public class Vehicle {

    VehicleStatus vehicleStatus;

    @Id
    @GeneratedValue
    private Long vehicleID;

    private VehicleType vehicleType;
    private VehicleFeatures vehicleFeatures;

    private String[] pictureURLs;
    private String[] particularities;


    public Vehicle(VehicleType vehicleType){
        this.vehicleStatus = VehicleStatus.AVAILABLE;
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "Vehicle: " +
                "vehicleStatus=" + vehicleStatus +
                ", vehicleID=" + vehicleID +
                ", vehicleType=" + vehicleType +
                ", pictureURLs=" + Arrays.toString(pictureURLs) +
                ", particularities='" + particularities + '\'';
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
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
