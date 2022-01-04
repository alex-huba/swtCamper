package swtcamper.backend.entities;

public interface IPicture {
    long getPictureID();
    void setPictureID(long pictureID);
    long getVehicleID();
    void setVehicleID(long vehicleID);
    byte[] getByteArray();
    void setByteArray(byte[] byteArray);
}
