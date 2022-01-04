package swtcamper.api.contract;

public class PictureDTO {
    private long pictureID;

    private long vehicleID;

    private byte[] byteArray;

    public PictureDTO(long pictureID, long vehicleID, byte[] byteArray) {
        this.pictureID = pictureID;
        this.vehicleID = vehicleID;
        this.byteArray = byteArray;
    }

    public PictureDTO(long pictureID, byte[] byteArray) {
        this.pictureID = pictureID;
        this.byteArray = byteArray;
    }

    public PictureDTO(byte[] byteArray) {
        this.byteArray = byteArray;
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
