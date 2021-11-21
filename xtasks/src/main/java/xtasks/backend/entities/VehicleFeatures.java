package xtasks.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VehicleFeatures {

  @Id
  @GeneratedValue
  private Long vehicleFeaturesID;

  Long vehicleID;

  private VehicleType vehicleType;

  //Generelle Überlegung: Macht es mehr Sinn, die Attribute alle auf public bzw. default zu setzen aber dafür als final?
  // So kann man sich die ganzen Getter und Setter sparen
  // Auf der anderen Seite geht dabei die Flexibilität verloren, im Nachhinein noch was zu ändern
  // (falls z.B. noch eine Dusche oder ein WC nachträglich eingebaut wird)

  private String make;
  private String model;
  private String year;

  private double length;
  private double width;
  private double height;

  private String engine;
  private String transmission;

  private int seats;
  private int beds;

  private boolean roofTent;
  private boolean roofRack;
  private boolean bikeRack;
  private boolean shower;
  private boolean toilet;
  private boolean kitchenUnit;
  private boolean fridge;

  public VehicleFeatures(Long vehicleID) {
    this.vehicleID = vehicleID;
  }

  public Long getVehicleFeaturesID() {
    return vehicleFeaturesID;
  }

  public void setVehicleFeaturesID(Long vehicleFeaturesID) {
    this.vehicleFeaturesID = vehicleFeaturesID;
  }

  public Long getVehicleID() {
    return vehicleID;
  }

  public void setVehicleID(Long vehicleID) {
    this.vehicleID = vehicleID;
  }

  public VehicleType getVehicleType() {
    return vehicleType;
  }

  public void setVehicleType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public VehicleType getType() {
    return vehicleType;
  }

  public void setType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public double getLength() {
    return length;
  }

  public void setLength(double length) {
    this.length = length;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public String getEngine() {
    return engine;
  }

  public void setEngine(String engine) {
    this.engine = engine;
  }

  public String getTransmission() {
    return transmission;
  }

  public void setTransmission(String transmission) {
    this.transmission = transmission;
  }

  public int getSeats() {
    return seats;
  }

  public void setSeats(int seats) {
    this.seats = seats;
  }

  public int getBeds() {
    return beds;
  }

  public void setBeds(int beds) {
    this.beds = beds;
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

  public boolean isKitchenUnit() {
    return kitchenUnit;
  }

  public void setKitchenUnit(boolean kitchenUnit) {
    this.kitchenUnit = kitchenUnit;
  }

  public boolean isFridge() {
    return fridge;
  }

  public void setFridge(boolean fridge) {
    this.fridge = fridge;
  }
}
