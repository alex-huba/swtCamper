package swtcamper.backend.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.PictureDTO;
import swtcamper.backend.entities.Picture;
import swtcamper.backend.repositories.PictureRepository;

@RunWith(MockitoJUnitRunner.class)
public class PictureServiceTest {

  @InjectMocks
  private PictureService pictureServiceUnderTest;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private PictureRepository pictureRepository;

  private PictureDTO giveMeTestPictureDTO() {
    PictureDTO testPictureDTO = new PictureDTO(1001, 505, "/path/folder/");
    return testPictureDTO;
  }

  private PictureDTO giveMeOneMoreTestPictureDTO() {
    PictureDTO testPictureDTO = new PictureDTO(2002, 808, "/abc/directory/");
    return testPictureDTO;
  }

  private Picture giveMeTestPicture() {
    Picture testPicture = new Picture();
    testPicture.setPictureID(1001);
    testPicture.setVehicleID(505);
    testPicture.setPath("/path/folder/");
    return testPicture;
  }

  private Picture giveMeOneMoreTestPicture() {
    Picture testPicture = new Picture();
    testPicture.setPictureID(2002);
    testPicture.setVehicleID(808);
    testPicture.setPath("/abc/directory/");
    return testPicture;
  }

  private List<Picture> giveMeListOfPictures() {
    List<Picture> pictures = new ArrayList<>();
    pictures.add(giveMeTestPicture());
    pictures.add(giveMeOneMoreTestPicture());
    return pictures;
  }

  private List<PictureDTO> giveMeListOfPicturesDTO() {
    List<PictureDTO> pictureDTOS = new ArrayList<>();
    pictureDTOS.add(giveMeTestPictureDTO());
    pictureDTOS.add(giveMeOneMoreTestPictureDTO());
    return pictureDTOS;
  }

  private void mockPictureDTOToPicture(PictureDTO pictureDTO) {
    doReturn(giveMeTestPicture()).when(modelMapper).pictureDTOToPicture(any());
  }

  private void mockPictureToPictureDTO(Picture picture) {
    doReturn(giveMeTestPictureDTO())
      .when(modelMapper)
      .pictureToPictureDTO(any());
  }

  @BeforeEach
  public void saveTestPicture() {
    when(pictureRepository.save(giveMeTestPicture()))
      .thenReturn(giveMeTestPicture());
  }

  @Test
  public void whenCreatePictureItShouldReturnPictureDTO() {
    // Given
    mockPictureDTOToPicture(giveMeTestPictureDTO());
    mockPictureToPictureDTO(giveMeTestPicture());

    // When
    pictureServiceUnderTest.create(giveMeTestPictureDTO());

    ArgumentCaptor<Picture> pictureArgumentCaptor = ArgumentCaptor.forClass(
      Picture.class
    );

    verify(pictureRepository, times(1)).save(pictureArgumentCaptor.capture());

    // Then
    assertEquals(
      giveMeTestPicture().getPictureID(),
      pictureArgumentCaptor.getValue().getPictureID()
    );
  }

  @Test
  public void whenGetPicturesItShouldReturnPicturesDTO() {
    when(pictureRepository.findAll()).thenReturn(giveMeListOfPictures());
    when(modelMapper.picturesToPictureDTOs(any()))
      .thenReturn(giveMeListOfPicturesDTO());

    List<PictureDTO> actual = pictureServiceUnderTest.getPicturesForVehicle(
      giveMeTestPictureDTO().getVehicleID()
    );
    PictureDTO actualPicture = actual.get(0);

    List<PictureDTO> expected = new ArrayList<>();
    expected.add(giveMeTestPictureDTO());
    PictureDTO expectedPicture = expected.get(0);

    assertNotNull(actual);
    assertEquals(expectedPicture.getPictureID(), actualPicture.getPictureID());
  }

  @Test
  public void whenDeletePictureByIdItShouldReturnEmptyRepo() {
    pictureServiceUnderTest.deletePictureById(
      giveMeTestPicture().getPictureID()
    );

    verify(pictureRepository).deleteById(any());
  }
}
