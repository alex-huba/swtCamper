package swtcamper.backend.entities;

import javax.persistence.*;
import org.hibernate.annotations.Type;

@Entity
public class Vehicle {

  @Id
  @GeneratedValue
  private long vehicleID;

  @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
  @JoinColumn(name = "vehicle_features_id")
  private VehicleFeatures vehicleFeatures;

  public Vehicle(VehicleFeatures vehicleFeatures) {
    this.vehicleFeatures = vehicleFeatures;
  }

  public Vehicle() {
    super();
  }

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

}
