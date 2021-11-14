package xtasks.api.contract;

import java.util.Arrays;

public class VehicleDTO {

    private Long vehicleID;

    private VehicleTypeDTO vehicleType;

    private VehicleFeaturesDTO vehicleFeatures;

    private String[] pictureURLs;

    private String[] particularities;

    private VehicleStatusDTO vehicleStatus;

    public VehicleDTO() {
        super();
    }

    // Getters & Setters

    public Long getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Long vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleTypeDTO getVehicleType() {
        return vehicleType;
    }

    public void setVehicleTypeDTO(VehicleTypeDTO vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleFeaturesDTO getVehicleFeaturesDTO() {
        return vehicleFeatures;
    }

    public void setVehicleFeaturesDTO(VehicleFeaturesDTO vehicleFeatures) {
        this.vehicleFeatures = vehicleFeatures;
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

    // Constructor

    public VehicleDTO(
            Long vehicleID,
            VehicleTypeDTO vehicleType,
            VehicleFeaturesDTO vehicleFeatures,
            String[] pictureURLs,
            String[] particularities,
            VehicleStatusDTO vehicleStatus
    ) {
        super();
        this.vehicleID = vehicleID;
        this.vehicleType = vehicleType;
        this.vehicleFeatures = vehicleFeatures;
        this.pictureURLs = pictureURLs;
        this.particularities = particularities;
        this.vehicleStatus = vehicleStatus
    }

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "vehicleID=" + vehicleID +
                ", vehicleType=" + vehicleType +
                ", vehicleFeatures=" + vehicleFeatures +
                ", pictureURLs=" + Arrays.toString(pictureURLs) +
                ", particularities=" + Arrays.toString(particularities) +
                '}';
    }
}
