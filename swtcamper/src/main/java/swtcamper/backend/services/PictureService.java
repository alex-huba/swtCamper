package swtcamper.backend.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.PictureDTO;
import swtcamper.backend.repositories.PictureRepository;

@Service
public class PictureService {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PictureRepository pictureRepository;

  public PictureDTO create(PictureDTO pictureDTO) {
    return modelMapper.pictureToPictureDTO(
      pictureRepository.save(modelMapper.pictureDTOToPicture(pictureDTO))
    );
  }

  public List<PictureDTO> getPicturesForVehicle(long vehicleId) {
    return modelMapper.picturesToPictureDTOs(
      pictureRepository
        .findAll()
        .stream()
        .filter(picture -> picture.getVehicleID() == vehicleId)
        .collect(Collectors.toList())
    );
  }

  public void deletePictureById(long id) {
    pictureRepository.deleteById(id);
  }
}
