package swtcamper.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import swtcamper.backend.entities.interfaces.IVehicle;

@Entity
public class Vehicle implements IVehicle {

  @Id
  @GeneratedValue
  private long vehicleID;

  private VehicleType vehicleType;

  private String make;
  private String model;
  private String year;

  private double length;
  private double width;
  private double height;

  private FuelType fuelType;
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
  public VehicleType getVehicleType() {
    return vehicleType;
  }

  @Override
  public void setVehicleType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
  }

  @Override
  public String getMake() {
    return make;
  }

  @Override
  public void setMake(String make) {
    this.make = make;
  }

  @Override
  public String getModel() {
    return model;
  }

  @Override
  public void setModel(String model) {
    this.model = model;
  }

  @Override
  public String getYear() {
    return year;
  }

  @Override
  public void setYear(String year) {
    this.year = year;
  }

  @Override
  public double getLength() {
    return length;
  }

  @Override
  public void setLength(double length) {
    this.length = length;
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public void setWidth(double width) {
    this.width = width;
  }

  @Override
  public double getHeight() {
    return height;
  }

  @Override
  public void setHeight(double height) {
    this.height = height;
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
  public String getTransmission() {
    return transmission;
  }

  @Override
  public void setTransmission(String transmission) {
    this.transmission = transmission;
  }

  @Override
  public int getSeats() {
    return seats;
  }

  @Override
  public void setSeats(int seats) {
    this.seats = seats;
  }

  @Override
  public int getBeds() {
    return beds;
  }

  @Override
  public void setBeds(int beds) {
    this.beds = beds;
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
  public boolean isKitchenUnit() {
    return kitchenUnit;
  }

  @Override
  public void setKitchenUnit(boolean kitchenUnit) {
    this.kitchenUnit = kitchenUnit;
  }

  @Override
  public boolean isFridge() {
    return fridge;
  }

  @Override
  public void setFridge(boolean fridge) {
    this.fridge = fridge;
  }
}
