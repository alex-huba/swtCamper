package swtcamper.backend.entities;

import java.time.LocalDate;
import swtcamper.backend.entities.interfaces.IFilter;

public class Filter implements IFilter {

  private String location;
  private VehicleType vehicleType;
  private String vehicleBrand;
  private int constructionYear;
  private int maxPricePerDay;
  private FuelType fuelType;
  private TransmissionType transmissionType;
  private int seatAmount;
  private int bedAmount;
  private boolean roofTent;
  private boolean roofRack;
  private boolean bikeRack;
  private boolean shower;
  private boolean toilet;
  private boolean kitchen;
  private boolean fridge;
  private LocalDate startDate;
  private LocalDate endDate;

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public VehicleType getVehicleType() {
    return vehicleType;
  }

  @Override
  public void setVehicleType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  @Override
  public String getVehicleBrand() {
    return vehicleBrand;
  }

  @Override
  public void setVehicleBrand(String vehicleBrand) {
    this.vehicleBrand = vehicleBrand;
  }

  @Override
  public int getConstructionYear() {
    return constructionYear;
  }

  @Override
  public void setConstructionYear(int constructionYear) {
    this.constructionYear = constructionYear;
  }

  @Override
  public int getMaxPricePerDay() {
    return maxPricePerDay;
  }

  @Override
  public void setMaxPricePerDay(int maxPricePerDay) {
    this.maxPricePerDay = maxPricePerDay;
  }

  @Override
  public FuelType getFuelType() {
    return fuelType;
  }

  @Override
  public void setFuelType(FuelType fuelType) {
    this.fuelType = fuelType;
  }

  @Override
  public TransmissionType getTransmissionType() {
    return transmissionType;
  }

  @Override
  public void setTransmissionType(TransmissionType transmissionType) {
    this.transmissionType = transmissionType;
  }

  @Override
  public int getSeatAmount() {
    return seatAmount;
  }

  @Override
  public void setSeatAmount(int seatAmount) {
    this.seatAmount = seatAmount;
  }

  @Override
  public int getBedAmount() {
    return bedAmount;
  }

  @Override
  public void setBedAmount(int bedAmount) {
    this.bedAmount = bedAmount;
  }

  @Override
  public boolean isRoofTent() {
    return roofTent;
  }

  @Override
  public void setRoofTent(boolean roofTent) {
    this.roofTent = roofTent;
  }

  @Override
  public boolean isRoofRack() {
    return roofRack;
  }

  @Override
  public void setRoofRack(boolean roofRack) {
    this.roofRack = roofRack;
  }

  @Override
  public boolean isBikeRack() {
    return bikeRack;
  }

  @Override
  public void setBikeRack(boolean bikeRack) {
    this.bikeRack = bikeRack;
  }

  @Override
  public boolean isShower() {
    return shower;
  }

  @Override
  public void setShower(boolean shower) {
    this.shower = shower;
  }

  @Override
  public boolean isToilet() {
    return toilet;
  }

  @Override
  public void setToilet(boolean toilet) {
    this.toilet = toilet;
  }

  @Override
  public boolean isKitchen() {
    return kitchen;
  }

  @Override
  public void setKitchen(boolean kitchen) {
    this.kitchen = kitchen;
  }

  @Override
  public boolean isFridge() {
    return fridge;
  }

  @Override
  public void setFridge(boolean fridge) {
    this.fridge = fridge;
  }

  @Override
  public LocalDate getStartDate() {
    return startDate;
  }

  @Override
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  @Override
  public LocalDate getEndDate() {
    return endDate;
  }

  @Override
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean isEmpty() {
    return (
      location == null &&
      vehicleType == null &&
      vehicleBrand == null &&
      constructionYear == 0 &&
      maxPricePerDay == 0 &&
      fuelType == null &&
      transmissionType == null &&
      seatAmount == 0 &&
      bedAmount == 0 &&
      !roofTent &&
      !roofRack &&
      !bikeRack &&
      !shower &&
      !toilet &&
      !kitchen &&
      !fridge &&
      startDate == null &&
      endDate == null
    );
  }

  @Override
  public String toString() {
    return (
      "Filter{" +
      "location='" +
      location +
      '\'' +
      ", vehicleType=" +
      vehicleType +
      ", vehicleBrand='" +
      vehicleBrand +
      '\'' +
      ", constructionYear=" +
      constructionYear +
      ", maxPricePerDay=" +
      maxPricePerDay +
      ", engine='" +
      fuelType +
      '\'' +
      ", transmissionType=" +
      transmissionType +
      ", seatAmount=" +
      seatAmount +
      ", bedAmount=" +
      bedAmount +
      ", roofTent=" +
      roofTent +
      ", roofRack=" +
      roofRack +
      ", bikeRack=" +
      bikeRack +
      ", shower=" +
      shower +
      ", toilet=" +
      toilet +
      ", kitchen=" +
      kitchen +
      ", fridge=" +
      fridge +
      '}'
    );
  }
}
