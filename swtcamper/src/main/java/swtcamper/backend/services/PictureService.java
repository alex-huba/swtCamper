package swtcamper.backend.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.Picture;
import swtcamper.backend.repositories.PictureRepository;

@Service
public class PictureService {

  @Autowired
  private PictureRepository pictureRepository;

  /**
   * Saves a new {@link Picture} to the database
   *
   * @param picture new Picture to create
   * @return DTO of newly saved Picture
   */
  public Picture create(Picture picture) {
    return pictureRepository.save(picture);
  }

  /**
   * Get all pictures that are related to a specific vehicle
   *
   * @param vehicleId ID of the vehicle to get the pictures for
   * @return List of available pictures for this vehicle
   */
  public List<Picture> getPicturesForVehicle(long vehicleId) {
    return pictureRepository
      .findAll()
      .stream()
      .filter(picture -> picture.getVehicleID() == vehicleId)
      .collect(Collectors.toList());
  }

  /**
   * Deletes a specific picture by its ID
   *
   * @param id ID of the picture to delete
   */
  public void deletePictureById(long id) {
    pictureRepository.deleteById(id);
  }
}
