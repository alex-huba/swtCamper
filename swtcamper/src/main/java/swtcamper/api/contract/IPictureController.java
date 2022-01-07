package swtcamper.api.contract;

import java.util.List;

public interface IPictureController {
  PictureDTO create(PictureDTO pictureDTO);
  List<PictureDTO> getPicturesForVehicle(long vehicleId);
  void deletePictureById(long id);
}
