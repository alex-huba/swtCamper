package swtcamper.backend.entities;

import java.nio.file.Path;

public interface IPicture {
  long getPictureID();
  void setPictureID(long pictureID);
  long getVehicleID();
  void setVehicleID(long vehicleID);
  String getPath();
  void setPath(String byteArray);
}
