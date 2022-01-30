package swtcamper.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import swtcamper.api.contract.PictureDTO;
import swtcamper.backend.entities.interfaces.IPicture;

@Entity
public class Picture implements IPicture {

  @Id
  @GeneratedValue
  private long pictureID;

  private long vehicleID;

  private String path;

  public Picture(PictureDTO pictureDTO) {
    this.pictureID = pictureDTO.getPictureID();
    this.vehicleID = pictureDTO.getVehicleID();
    this.path = pictureDTO.getPath();
  }

  public Picture(long vehicleID, String path) {
    this.vehicleID = vehicleID;
    this.path = path;
  }

  public Picture(String path) {
    this.path = path;
  }

  public Picture() {}

  @Override
  public long getPictureID() {
    return pictureID;
  }

  @Override
  public void setPictureID(long pictureID) {
    this.pictureID = pictureID;
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
  public String getPath() {
    return path;
  }

  @Override
  public void setPath(String path) {
    this.path = String.valueOf(path);
  }
}
