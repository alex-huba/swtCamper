package swtcamper.backend.entities.interfaces;

import swtcamper.backend.entities.VehicleFeatures;

public interface IVehicle {
  long getVehicleID();
  void setVehicleID(long vehicleID);
  VehicleFeatures getVehicleFeatures();
  void setVehicleFeatures(VehicleFeatures vehicleFeatures);
}
