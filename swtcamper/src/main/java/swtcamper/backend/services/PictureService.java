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
    //        List<PictureDTO> pictureList = new ArrayList<>();
    //        for (Picture picture : pictureRepository.findAll()) {
    //            if (picture.getVehicleID() == vehicleId) {
    //                System.out.println(picture.getPictureID());
    //                pictureList.add(modelMapper.pictureToPictureDTO(picture));
    //            } else {
    //                System.out.println(picture.getPictureID() + " + does not fit to vehicle " + vehicleId);
    //            }
    //        }
    //        return pictureList;
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
