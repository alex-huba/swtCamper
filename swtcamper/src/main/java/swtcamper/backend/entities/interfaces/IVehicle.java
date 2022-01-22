package swtcamper.backend.entities.interfaces;

import swtcamper.backend.entities.FuelType;
import swtcamper.backend.entities.VehicleType;

public interface IVehicle {
  long getVehicleID();
  void setVehicleID(long vehicleID);
  VehicleType getVehicleType();
  void setVehicleType(VehicleType vehicleType);
  String getMake();
  void setMake(String make);
  String getModel();
  void setModel(String model);
  String getYear();
  void setYear(String year);
  double getLength();
  void setLength(double length);
  double getWidth();
  void setWidth(double width);
  double getHeight();
  void setHeight(double height);
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
