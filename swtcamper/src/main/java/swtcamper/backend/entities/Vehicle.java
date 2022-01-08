package swtcamper.backend.entities;

import javax.persistence.*;

@Entity
public class Vehicle {

  @Id
  @GeneratedValue
  private long vehicleID;

  @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
  @JoinColumn(name = "vehicleFeatures_id")
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
