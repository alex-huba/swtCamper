package swtcamper.backend.entities;

import javax.persistence.*;
import swtcamper.backend.entities.interfaces.IVehicle;

@Entity
public class Vehicle implements IVehicle {

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

  @Override
  public long getVehicleID() {
    return vehicleID;
  }

  @Override
  public void setVehicleID(long vehicleID) {
    this.vehicleID = vehicleID;
  }

  @Override
  public VehicleFeatures getVehicleFeatures() {
    return vehicleFeatures;
  }

  @Override
  public void setVehicleFeatures(VehicleFeatures vehicleFeatures) {
    this.vehicleFeatures = vehicleFeatures;
  }
}
