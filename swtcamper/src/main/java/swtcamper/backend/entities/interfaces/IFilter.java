package swtcamper.backend.entities.interfaces;

import java.time.LocalDate;
import swtcamper.backend.entities.FuelType;
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.VehicleType;

public interface IFilter {
  String getLocation();

  void setLocation(String location);

  VehicleType getVehicleType();

  void setVehicleType(VehicleType vehicleType);

  String getVehicleBrand();

  void setVehicleBrand(String vehicleBrand);

  int getConstructionYear();

  void setConstructionYear(int constructionYear);

  int getMaxPricePerDay();

  void setMaxPricePerDay(int maxPricePerDay);

  FuelType getFuelType();

  void setFuelType(FuelType fuelType);

  TransmissionType getTransmissionType();

  void setTransmissionType(TransmissionType transmissionType);

  int getSeatAmount();

  void setSeatAmount(int seatAmount);

  int getBedAmount();

  void setBedAmount(int bedAmount);

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

  boolean isKitchen();

  void setKitchen(boolean kitchen);

  boolean isFridge();

  void setFridge(boolean fridge);

  LocalDate getStartDate();

  void setStartDate(LocalDate startDate);

  LocalDate getEndDate();

  void setEndDate(LocalDate endDate);

  boolean isEmpty();
}
