package swtcamper.backend.entities;

import java.time.LocalDate;

public class Filter {

  private String location;
  private LocalDate pickUpDate;
  private LocalDate returnDate;
  private VehicleType vehicleType;
  private String vehicleBrand;
  private int constructionYear;
  private int maxPricePerDay;
  private String engine;
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

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public LocalDate getPickUpDate() {
    return pickUpDate;
  }

  public void setPickUpDate(LocalDate pickUpDate) {
    this.pickUpDate = pickUpDate;
  }

  public LocalDate getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(LocalDate returnDate) {
    this.returnDate = returnDate;
  }

  public VehicleType getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  public String getVehicleBrand() {
    return vehicleBrand;
  }

  public void setVehicleBrand(String vehicleBrand) {
    this.vehicleBrand = vehicleBrand;
  }

  public int getConstructionYear() {
    return constructionYear;
  }

  public void setConstructionYear(int constructionYear) {
    this.constructionYear = constructionYear;
  }

  public int getMaxPricePerDay() {
    return maxPricePerDay;
  }

  public void setMaxPricePerDay(int maxPricePerDay) {
    this.maxPricePerDay = maxPricePerDay;
  }

  public String getEngine() {
    return engine;
  }

  public void setEngine(String engine) {
    this.engine = engine;
  }

  public TransmissionType getTransmissionType() {
    return transmissionType;
  }

  public void setTransmissionType(TransmissionType transmissionType) {
    this.transmissionType = transmissionType;
  }

  public int getSeatAmount() {
    return seatAmount;
  }

  public void setSeatAmount(int seatAmount) {
    this.seatAmount = seatAmount;
  }

  public int getBedAmount() {
    return bedAmount;
  }

  public void setBedAmount(int bedAmount) {
    this.bedAmount = bedAmount;
  }

  public boolean isRoofTent() {
    return roofTent;
  }

  public void setRoofTent(boolean roofTent) {
    this.roofTent = roofTent;
  }

  public boolean isRoofRack() {
    return roofRack;
  }

  public void setRoofRack(boolean roofRack) {
    this.roofRack = roofRack;
  }

  public boolean isBikeRack() {
    return bikeRack;
  }

  public void setBikeRack(boolean bikeRack) {
    this.bikeRack = bikeRack;
  }

  public boolean isShower() {
    return shower;
  }

  public void setShower(boolean shower) {
    this.shower = shower;
  }

  public boolean isToilet() {
    return toilet;
  }

  public void setToilet(boolean toilet) {
    this.toilet = toilet;
  }

  public boolean isKitchen() {
    return kitchen;
  }

  public void setKitchen(boolean kitchen) {
    this.kitchen = kitchen;
  }

  public boolean isFridge() {
    return fridge;
  }

  public void setFridge(boolean fridge) {
    this.fridge = fridge;
  }

  public String toString() {
    return (
      "location: " +
      location +
      "\n" +
      "pickUpDate: " +
      pickUpDate +
      "\n" +
      "returnDate: " +
      returnDate +
      "\n" +
      "vehicleType: " +
      vehicleType +
      "\n" +
      "vehicleBrand: " +
      vehicleBrand +
      "\n" +
      "constructionYear: " +
      constructionYear +
      "\n" +
      "maxPricePerDay: " +
      maxPricePerDay +
      "\n" +
      "engine: " +
      engine +
      "\n" +
      "transmission: " +
      transmissionType +
      "\n" +
      "seatAmount: " +
      seatAmount +
      "\n" +
      "bedAmount: " +
      bedAmount +
      "\n" +
      "roofTent: " +
      roofTent +
      "\n" +
      "roofRack: " +
      roofRack +
      "\n" +
      "bikeRack: " +
      bikeRack +
      "\n" +
      "shower: " +
      shower +
      "\n" +
      "toilet: " +
      toilet +
      "\n" +
      "kitchen: " +
      kitchen +
      "\n" +
      "fridge: " +
      fridge
    );
  }
}
