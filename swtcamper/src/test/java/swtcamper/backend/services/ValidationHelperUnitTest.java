package swtcamper.backend.services;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.controller.ValidationHelper;
import swtcamper.backend.entities.Picture;
import swtcamper.backend.repositories.PictureRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValidationHelperUnitTest {

  @InjectMocks
  private ValidationHelper validationHelperunderTest;

  @Test
  public void checkOfferTitleIsCorrect () {
    //Given
    List<String> rightTitles = Arrays.asList(new String[]{"good Title", "another Title", "kjsdhnfk 234 12k3j4nk"});
    List<String> wrongTitles= Arrays.asList(new String[]{"", "5sd6", "dfg"});

    for (String rightTitle : rightTitles) {
      assertTrue(validationHelperunderTest.checkOfferTitle(rightTitle), true);
    }
    for (String wrongTitle : wrongTitles) {
      assertTrue(validationHelperunderTest.checkOfferTitle(wrongTitle), false);
    }
  }

}
