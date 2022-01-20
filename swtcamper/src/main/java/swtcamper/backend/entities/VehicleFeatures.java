package swtcamper.backend.entities;

import java.util.Objects;
import javax.persistence.*;
import swtcamper.backend.entities.interfaces.IVehicleFeatures;

@Entity
public class VehicleFeatures implements IVehicleFeatures {

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

  @Override
  public long getVehicleFeaturesID() {
    return vehicleFeaturesID;
  }

  @Override
  public void setVehicleFeaturesID(long vehicleFeaturesID) {
    this.vehicleFeaturesID = vehicleFeaturesID;
  }

  @Override
  public Vehicle getVehicle() {
    return vehicle;
  }

  @Override
  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
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
  public VehicleType getType() {
    return vehicleType;
  }

  @Override
  public void setType(VehicleType vehicleType) {
    this.vehicleType = vehicleType;
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
  public double getHeight() {
    return height;
  }

  @Override
  public void setHeight(double height) {
    this.height = height;
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
