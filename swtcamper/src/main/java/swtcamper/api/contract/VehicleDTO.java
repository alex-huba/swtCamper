package xtasks.api.contract;

import java.util.Arrays;

public class VehicleDTO {

    private Long vehicleID;

    private VehicleTypeDTO vehicleType;

    private Long vehicleFeaturesID;

    private String[] pictureURLs;

    private String[] particularities;

    private AvailabilityDTO availability;

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

    public Long getVehicleFeaturesID() {
        return vehicleFeaturesID;
    }

    public void setVehicleFeaturesID(long vehicleFeaturesID) {
        this.vehicleFeaturesID = vehicleFeaturesID;
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

    public VehicleDTO() { super(); }

    public VehicleDTO(
            Long vehicleID,
            VehicleTypeDTO vehicleType,
            Long vehicleFeaturesID,
            String[] pictureURLs,
            String[] particularities,
            AvailabilityDTO availability
    ) {
        super();
        this.vehicleID = vehicleID;
        this.vehicleType = vehicleType;
        this.vehicleFeaturesID = vehicleFeaturesID;
        this.pictureURLs = pictureURLs;
        this.particularities = particularities;
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "vehicleID=" + vehicleID +
                ", vehicleType=" + vehicleType +
                ", vehicleFeaturesID=" + vehicleFeaturesID +
                ", pictureURLs=" + Arrays.toString(pictureURLs) +
                ", particularities=" + Arrays.toString(particularities) +
                '}';
    }
}
