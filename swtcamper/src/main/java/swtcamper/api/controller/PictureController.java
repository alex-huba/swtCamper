package swtcamper.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.contract.interfaces.IPictureController;
import swtcamper.backend.services.PictureService;

@Component
public class PictureController implements IPictureController {

  @Autowired
  private PictureService pictureService;

  public PictureDTO create(PictureDTO pictureDTO) {
    return pictureService.create(pictureDTO);
  }

  public List<PictureDTO> getPicturesForVehicle(long vehicleId) {
    return pictureService.getPicturesForVehicle(vehicleId);
  }

  public void deletePictureById(long id) {
    pictureService.deletePictureById(id);
  }
}
