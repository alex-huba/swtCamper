package swtcamper.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Picture implements IPicture {

    @Id
    @GeneratedValue
    private long pictureID;

    private long vehicleID;

    private byte[] byteArray;

    public Picture(long vehicleID, byte[] byteArray) {
        this.vehicleID = vehicleID;
        this.byteArray = byteArray;
    }

    public Picture(byte[] byteArray) {
        this.byteArray = byteArray;
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

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}
