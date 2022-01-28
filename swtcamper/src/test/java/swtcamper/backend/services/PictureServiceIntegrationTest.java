package swtcamper.backend.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.PictureDTO;
import swtcamper.backend.entities.Picture;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.repositories.PictureRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PictureServiceIntegrationTest {

  @Autowired
  private PictureRepository pictureRepository;

  @Autowired
  private PictureService pictureService;

  @Autowired
  private ModelMapper modelMapper;

  @Test
  public void getPicturesForVehicleShouldReturnCorrectPictures() {
    // creating vehicle under test
    Vehicle vehicle = new Vehicle();
    vehicle.setVehicleID(123);

    // creating picture for vehicle and saving it in repo
    Picture picture = new Picture();
    picture.setVehicleID(vehicle.getVehicleID());
    picture.setPath("/123/abc");
    picture.setPictureID(8008);
    pictureRepository.save(picture);

    // testing method of get-method from pictureService
    List<PictureDTO> list = pictureService.getPicturesForVehicle(
      vehicle.getVehicleID()
    );

    // checking whether the operation was successful
    assertEquals(1, list.size());
    assertEquals(vehicle.getVehicleID(), list.get(0).getVehicleID());
  }

  @Test
  public void createPictureShouldStorePictureInDatabase() {
    // creating pictureDTO under test
    PictureDTO pictureDTO = new PictureDTO(8008, 123, "/123/abc");

    //testing method create
    pictureService.create(pictureDTO);

    // checking whether the operation was successful
    List<PictureDTO> list = pictureService.getPicturesForVehicle(
      pictureDTO.getVehicleID()
    );
    assertEquals(1, list.size());
    assertEquals(pictureDTO.getVehicleID(), list.get(0).getVehicleID());
  }

  @Test
  public void deletePictureByIdShouldDeletePictureByGivenId() {
    // creating picture under test
    Vehicle vehicle = new Vehicle();
    vehicle.setVehicleID(123);

    PictureDTO pictureDTO = new PictureDTO(
      8008,
      vehicle.getVehicleID(),
      "/123/abc"
    );
    pictureService.create(pictureDTO);
    List<PictureDTO> list = pictureService.getPicturesForVehicle(
      pictureDTO.getVehicleID()
    );

    //testing method delete
    pictureService.deletePictureById(list.get(0).getPictureID());
    List<PictureDTO> emptyList = pictureService.getPicturesForVehicle(
      pictureDTO.getVehicleID()
    );

    // checking whether the operation was successful
    assertEquals(0, emptyList.size());
  }

  @Test
  public void pictureServiceShouldReturnEmptyListIfThereAreNoPicturesForGivenVehicle() {
    //creating random vehicle without assigned pictures
    Vehicle vehicle = new Vehicle();
    vehicle.setVehicleID(123);

    // trying to fetch pictures for the given test-vehicle
    List<PictureDTO> list = pictureService.getPicturesForVehicle(
      vehicle.getVehicleID()
    );

    //checking whether method works fine
    assertTrue(list.isEmpty());
  }
}
