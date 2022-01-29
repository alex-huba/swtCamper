package swtcamper.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.contract.interfaces.IPictureController;
import swtcamper.backend.entities.Picture;
import swtcamper.backend.services.PictureService;

@Component
public class PictureController implements IPictureController {

  @Autowired
  private PictureService pictureService;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public PictureDTO create(Picture picture) {
    return modelMapper.pictureToPictureDTO(pictureService.create(picture));
  }

  @Override
  public List<PictureDTO> getPicturesForVehicle(long vehicleId) {
    return modelMapper.picturesToPictureDTOs(
      pictureService.getPicturesForVehicle(vehicleId)
    );
  }

  @Override
  public void deletePictureById(long id) {
    pictureService.deletePictureById(id);
  }
}
