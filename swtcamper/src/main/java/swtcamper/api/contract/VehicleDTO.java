package swtcamper.api.contract;

import java.util.Arrays;

public class VehicleDTO {

  private long vehicleID;

  private VehicleTypeDTO vehicleType;

  private long vehicleFeaturesID;

  private String[] particularities;

  // Getters & Setters

  public long getVehicleID() {
    return vehicleID;
  }

  public void setVehicleID(long vehicleID) {
    this.vehicleID = vehicleID;
  }

  public VehicleTypeDTO getVehicleType() {
    return vehicleType;
  }

  public void setVehicleTypeDTO(VehicleTypeDTO vehicleType) {
    this.vehicleType = vehicleType;
  }

  public long getVehicleFeaturesID() {
    return vehicleFeaturesID;
  }

  public void setVehicleFeaturesID(long vehicleFeaturesID) {
    this.vehicleFeaturesID = vehicleFeaturesID;
  }

  public String[] getParticularities() {
    return particularities;
  }

  public void setParticularities(String[] particularities) {
    this.particularities = particularities;
  }

  // Constructor

  public VehicleDTO() {
    super();
  }

  public VehicleDTO(
    long vehicleID,
    VehicleTypeDTO vehicleType,
    long vehicleFeaturesID,
    String[] particularities
  ) {
    super();
    this.vehicleID = vehicleID;
    this.vehicleType = vehicleType;
    this.vehicleFeaturesID = vehicleFeaturesID;
    this.particularities = particularities;
  }
}
