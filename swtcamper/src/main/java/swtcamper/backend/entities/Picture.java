package swtcamper.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.nio.file.Path;

@Entity
public class Picture implements IPicture {

    @Id
    @GeneratedValue
    private long pictureID;

    private long vehicleID;

    private String path;

    public Picture(long vehicleID, String path) {
        this.vehicleID = vehicleID;
        this.path = path;
    }

    public Picture(String path) {
        this.path = path;
    }

    public Picture() {
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
        this.path = String.valueOf(path);
    }
}
