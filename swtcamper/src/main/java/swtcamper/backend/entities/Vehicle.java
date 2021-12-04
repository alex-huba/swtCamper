package swtcamper.backend.entities;

import java.util.Arrays;
import java.util.Objects;
import javax.persistence.*;



@Entity
public class Vehicle {

  @Id
  @GeneratedValue
  private long vehicleID;

  @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
  @JoinColumn(name="vehicleFeatures_id")
  private VehicleFeatures vehicleFeatures;

  private String[] pictureURLs;
  private String[] particularities;

  public Vehicle(VehicleFeatures vehicleFeatures) {
    this.vehicleFeatures = vehicleFeatures;
  }

  public Vehicle() {
    super();
  }

//  @Override
//  public String toString() {
//    return (
//      "Vehicle{" +
//      "vehicleID=" +
//      vehicleID +
//      ", vehicleFeatures=" +
//      vehicleFeatures +
//      ", pictureURLs=" +
//      Arrays.toString(pictureURLs) +
//      ", particularities=" +
//      Arrays.toString(particularities) +
//      '}'
//    );
//  }

  public long getVehicleID() {
    return vehicleID;
  }

  public void setVehicleID(long vehicleID) {
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

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    Vehicle vehicle = (Vehicle) o;
//    return vehicleID == vehicle.vehicleID && Objects.equals(vehicleFeatures, vehicle.vehicleFeatures) && Arrays.equals(pictureURLs, vehicle.pictureURLs) && Arrays.equals(particularities, vehicle.particularities);
//  }

//  @Override
//  public int hashCode() {
//    int result = Objects.hash(vehicleID, vehicleFeatures);
//    result = 31 * result + Arrays.hashCode(pictureURLs);
//    result = 31 * result + Arrays.hashCode(particularities);
//    return result;
//  }
}
