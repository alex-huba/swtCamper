package swtcamper.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.PictureDTO;
import swtcamper.backend.entities.Picture;
import swtcamper.backend.repositories.PictureRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PictureService {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PictureRepository pictureRepository;

  /**
   * Saves a new {@link Picture} to the database
   * @param pictureDTO DTO of new Picture to create
   * @return DTO of newly saved Picture
   */
  public PictureDTO create(PictureDTO pictureDTO) {
    return modelMapper.pictureToPictureDTO(
      pictureRepository.save(modelMapper.pictureDTOToPicture(pictureDTO))
    );
  }

  /**
   * Get all pictures that are related to a specific vehicle
   * @param vehicleId ID of the vehicle to get the pictures for
   * @return List of available pictures for this vehicle
   */
  public List<PictureDTO> getPicturesForVehicle(long vehicleId) {
    return modelMapper.picturesToPictureDTOs(
      pictureRepository
        .findAll()
        .stream()
        .filter(picture -> picture.getVehicleID() == vehicleId)
        .collect(Collectors.toList())
    );
  }

  /**
   * Deletes a specific picture by its ID
   * @param id ID of the picture to delete
   */
  public void deletePictureById(long id) {
    pictureRepository.deleteById(id);
  }
}
