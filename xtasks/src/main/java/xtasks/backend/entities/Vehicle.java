package xtasks.backend.entities;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Vehicle {

  @Id
  @GeneratedValue
  private Long vehicleID;

  // VehicleType lieber in VehicleFeatures?
  private VehicleType vehicleType;

  @OneToMany
  private VehicleFeatures vehicleFeatures;

  // pictureURLs und particularities lieber in VehicleFeatures?
  private String[] pictureURLs;
  private String[] particularities;

  public Vehicle(VehicleType vehicleType, VehicleFeatures vehicleFeatures) {
    this.vehicleType = vehicleType;
    this.vehicleFeatures = vehicleFeatures;
  }

  public Vehicle(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  public Vehicle() {
    super();
  }

  @Override
  public String toString() {
    return "Vehicle{" +
            "vehicleID=" + vehicleID +
            ", vehicleType=" + vehicleType +
            ", vehicleFeatures=" + vehicleFeatures +
            ", pictureURLs=" + Arrays.toString(pictureURLs) +
            ", particularities=" + Arrays.toString(particularities) +
            '}';
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
