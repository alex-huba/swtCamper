package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.IPictureController;
import swtcamper.api.contract.PictureDTO;
import swtcamper.backend.services.PictureService;

import java.util.List;

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
