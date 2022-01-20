package swtcamper.backend.entities.interfaces;

import swtcamper.backend.entities.FuelType;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;

public interface IVehicleFeatures {
  long getVehicleFeaturesID();

  void setVehicleFeaturesID(long vehicleFeaturesID);

  Vehicle getVehicle();

  void setVehicle(Vehicle vehicle);

  VehicleType getVehicleType();

  void setVehicleType(VehicleType vehicleType);

  String getMake();

  void setMake(String make);

  VehicleType getType();

  void setType(VehicleType vehicleType);

  String getModel();

  void setModel(String model);

  String getYear();

  void setYear(String year);

  double getLength();

  void setLength(double length);

  double getHeight();

  void setHeight(double height);

  double getWidth();

  void setWidth(double width);

  FuelType getFuelType();

  void setFuelType(FuelType fuelType);

  String getTransmission();

  void setTransmission(String transmission);

  int getSeats();

  void setSeats(int seats);

  int getBeds();

  void setBeds(int beds);

  boolean isRoofTent();

  void setRoofTent(boolean roofTent);

  boolean isRoofRack();

  void setRoofRack(boolean roofRack);

  boolean isBikeRack();

  void setBikeRack(boolean bikeRack);

  boolean isShower();

  void setShower(boolean shower);

  boolean isToilet();

  void setToilet(boolean toilet);

  boolean isKitchenUnit();

  void setKitchenUnit(boolean kitchenUnit);

  boolean isFridge();

  void setFridge(boolean fridge);
}
