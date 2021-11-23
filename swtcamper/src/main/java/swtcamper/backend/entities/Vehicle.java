package swtcamper.backend.entities;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Vehicle {

  @Id
  @GeneratedValue
  private Long vehicleID;

  @ManyToOne
  private VehicleFeatures vehicleFeatures;

  private String[] pictureURLs;
  private String[] particularities;

  public Vehicle(VehicleFeatures vehicleFeatures) {
    this.vehicleFeatures = vehicleFeatures;
  }

  public Vehicle() {
    super();
  }

  @Override
  public String toString() {
    return (
      "Vehicle{" +
      "vehicleID=" +
      vehicleID +
      ", vehicleFeatures=" +
      vehicleFeatures +
      ", pictureURLs=" +
      Arrays.toString(pictureURLs) +
      ", particularities=" +
      Arrays.toString(particularities) +
      '}'
    );
  }

  public Long getVehicleID() {
    return vehicleID;
  }

  public void setVehicleID(Long vehicleID) {
    this.vehicleID = vehicleID;
  }

  public VehicleFeatures getVehicleFeatures() {
    return vehicleFeatures;
  }

  public void setVehicleFeatures(VehicleFeatures vehicleFeatures) {
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
}
