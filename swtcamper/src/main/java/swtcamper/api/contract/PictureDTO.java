package swtcamper.api.contract;

import java.nio.file.Path;

public class PictureDTO {

  private long pictureID;

  private long vehicleID;

  private String path;

  public PictureDTO(long pictureID, long vehicleID, String path) {
    this.pictureID = pictureID;
    this.vehicleID = vehicleID;
    this.path = path;
  }

  public PictureDTO(long pictureID, String path) {
    this.pictureID = pictureID;
    this.path = path;
  }

  public PictureDTO(String path) {
    this.path = path;
  }

  public long getPictureID() {
    return pictureID;
  }

  public void setPictureID(long pictureID) {
    this.pictureID = pictureID;
  }

  public long getVehicleID() {
    return vehicleID;
  }

  public void setVehicleID(long vehicleID) {
    this.vehicleID = vehicleID;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
