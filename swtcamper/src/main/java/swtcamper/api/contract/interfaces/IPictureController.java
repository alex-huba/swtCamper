package swtcamper.api.contract.interfaces;

import java.util.List;
import swtcamper.api.contract.PictureDTO;

public interface IPictureController {
  PictureDTO create(PictureDTO pictureDTO);

  List<PictureDTO> getPicturesForVehicle(long vehicleId);

  void deletePictureById(long id);
}
