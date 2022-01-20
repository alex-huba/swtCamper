package swtcamper.backend.entities;

import java.util.Objects;
import javax.persistence.*;

@Entity
public class VehicleFeatures {

  @Id
  @GeneratedValue
  private long vehicleFeaturesID;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "vehicle_id")
  private Vehicle vehicle;

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

  public VehicleFeatures() {}

  public VehicleFeatures(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  public long getVehicleFeaturesID() {
    return vehicleFeaturesID;
  }

  public void setVehicleFeaturesID(long vehicleFeaturesID) {
    this.vehicleFeaturesID = vehicleFeaturesID;
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
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

  public FuelType getFuelType() {
    return fuelType;
  }

  public void setFuelType(FuelType fuelType) {
    this.fuelType = fuelType;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VehicleFeatures that = (VehicleFeatures) o;
    return (
      vehicleFeaturesID == that.vehicleFeaturesID &&
      Double.compare(that.length, length) == 0 &&
      Double.compare(that.width, width) == 0 &&
      Double.compare(that.height, height) == 0 &&
      seats == that.seats &&
      beds == that.beds &&
      roofTent == that.roofTent &&
      roofRack == that.roofRack &&
      bikeRack == that.bikeRack &&
      shower == that.shower &&
      toilet == that.toilet &&
      kitchenUnit == that.kitchenUnit &&
      fridge == that.fridge &&
      Objects.equals(vehicle, that.vehicle) &&
      vehicleType == that.vehicleType &&
      Objects.equals(make, that.make) &&
      Objects.equals(model, that.model) &&
      Objects.equals(year, that.year) &&
      Objects.equals(fuelType, that.fuelType) &&
      Objects.equals(transmission, that.transmission)
    );
  }
}
