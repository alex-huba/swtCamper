package swtcamper.api.contract.interfaces;

import java.util.List;
import swtcamper.api.contract.PictureDTO;
import swtcamper.backend.entities.Picture;

public interface IPictureController {
  PictureDTO create(Picture picture);

  List<PictureDTO> getPicturesForVehicle(long vehicleId);

  void deletePictureById(long id);
}
