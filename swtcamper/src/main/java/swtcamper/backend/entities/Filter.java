package swtcamper.backend.entities;

public class Filter {

  private String location;
  private VehicleType vehicleType;
  private String vehicleBrand;
  private int constructionYear;
  private int maxPricePerDay;
  private String engine;
  private TransmissionType transmissionType;
  private int seatAmount;
  private int bedAmount;
  private boolean excludeInactive;
  private boolean roofTent;
  private boolean roofRack;
  private boolean bikeRack;
  private boolean shower;
  private boolean toilet;
  private boolean kitchen;
  private boolean fridge;
  private boolean minAge21;
  private boolean crossingBordersAllowed;
  private boolean depositInCash;

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
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

  public boolean isExcludeInactive() {
    return excludeInactive;
  }

  public void setExcludeInactive(boolean excludeInactive) {
    this.excludeInactive = excludeInactive;
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

  public boolean isMinAge21() {
    return minAge21;
  }

  public void setMinAge21(boolean minAge21) {
    this.minAge21 = minAge21;
  }

  public boolean isCrossingBordersAllowed() {
    return crossingBordersAllowed;
  }

  public void setCrossingBordersAllowed(boolean crossingBordersAllowed) {
    this.crossingBordersAllowed = crossingBordersAllowed;
  }

  public boolean isDepositInCash() {
    return depositInCash;
  }

  public void setDepositInCash(boolean depositInCash) {
    this.depositInCash = depositInCash;
  }

  public boolean isEmpty() {
    return (
      location == null &&
      vehicleType == null &&
      vehicleBrand == null &&
      constructionYear == 0 &&
      maxPricePerDay == 0 &&
      engine == null &&
      transmissionType == null &&
      seatAmount == 0 &&
      bedAmount == 0 &&
      !excludeInactive &&
      !roofTent &&
      !roofRack &&
      !bikeRack &&
      !shower &&
      !toilet &&
      !kitchen &&
      !fridge &&
      !minAge21 &&
      !crossingBordersAllowed &&
      !depositInCash
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
      engine +
      '\'' +
      ", transmissionType=" +
      transmissionType +
      ", seatAmount=" +
      seatAmount +
      ", bedAmount=" +
      bedAmount +
      ", excludeInactive=" +
      excludeInactive +
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
      ", minAge21=" +
      minAge21 +
      ", crossingBordersAllowed=" +
      crossingBordersAllowed +
      ", depositInCash=" +
      depositInCash +
      '}'
    );
  }
}
