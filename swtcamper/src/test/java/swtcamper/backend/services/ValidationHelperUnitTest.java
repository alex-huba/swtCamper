package swtcamper.backend.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.api.controller.ValidationHelper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValidationHelperUnitTest {

  @InjectMocks
  private ValidationHelper validationHelperunderTest;

  @Test
  public void checkOfferTitleIsCorrect () {
    //Given
    List<String> rightTitles = Arrays.asList(new String[]{"good Title", "another Title"});
    List<String> wrongTitles= Arrays.asList(new String[]{"", "5sd6", "dfg", "Title with &%§<<#"});

    for (String rightTitle : rightTitles) {
      assertEquals(validationHelperunderTest.checkOfferTitle(rightTitle), true);
    }
    for (String wrongTitle : wrongTitles) {
      assertEquals(validationHelperunderTest.checkOfferTitle(wrongTitle), false);
    }
  }

  @Test
  public void checkOfferPriceIsCorrect () {
    //Given
    List<String> rightPrices = Arrays.asList(new String[]{"11", "233", "45", "10000"});
    List<String> wrongPrices= Arrays.asList(new String[]{"", "3", "dfg", "345353"});

    for (String rightPrice : rightPrices) {
      assertEquals(validationHelperunderTest.checkOfferPrice(rightPrice), true);
    }
    for (String wrongPrice : wrongPrices) {
      assertEquals(validationHelperunderTest.checkOfferPrice(wrongPrice), false);
    }
  }

  @Test
  public void checkOfferLocationIsCorrect () {
    //Given
    List<String> rightLocations = Arrays.asList(new String[]{"Bamberg", "Nürnberg"});
    List<String> wrongLocations= Arrays.asList(new String[]{"", "ad", "12"});

    for (String rightLocation : rightLocations) {
      assertEquals(validationHelperunderTest.checkOfferLocation(rightLocation), true);
    }
    for (String wrongLocation : wrongLocations) {
      assertEquals(validationHelperunderTest.checkOfferLocation(wrongLocation), false);
    }
  }

  @Test
  public void checkOfferContactIsCorrect () {
    //Given
    List<String> rightLocations = Arrays.asList(new String[]{"32452345", "2345345"});
    List<String> wrongLocations= Arrays.asList(new String[]{"", "ad", "12"});

    for (String rightLocation : rightLocations) {
      assertEquals(validationHelperunderTest.checkOfferContact(rightLocation), true);
    }
    for (String wrongLocation : wrongLocations) {
      assertEquals(validationHelperunderTest.checkOfferContact(wrongLocation), false);
    }
  }

  @Test
  public void checkUsernameIsCorrect () {
    //Given
    List<String> rightUsernames = Arrays.asList(new String[]{"Patrick", "Stefan1201"});
    List<String> wrongUsernames = Arrays.asList(new String[]{"", "adsd", "Patrick%&$"});

    for (String rightUsername : rightUsernames) {
      assertEquals(validationHelperunderTest.checkUserName(rightUsername), true);
    }
    for (String wrongUsername : wrongUsernames) {
      assertEquals(validationHelperunderTest.checkUserName(wrongUsername), false);
    }
  }

  @Test
  public void checkPasswordIsCorrect () {
    //Given
    List<String> rightPasswords = Arrays.asList(new String[]{"password-12", "test12$%"});
    List<String> wrongPasswords = Arrays.asList(new String[]{"","23ad", "password"});

    for (String rightPassword : rightPasswords) {
      assertEquals(validationHelperunderTest.checkPassword(rightPassword), true);
    }
    for (String wrongPassword : wrongPasswords) {
      assertEquals(validationHelperunderTest.checkPassword(wrongPassword), false);
    }
  }

  @Test
  public void checkEmailIsCorrect () {
    //Given
    List<String> rightEmails = Arrays.asList(new String[]{"test@mail.de", "test.test@mail.com"});
    List<String> wrongEmails = Arrays.asList(new String[]{"", "adsd", "test@mail", "test.de"});

    for (String rightEmail : rightEmails) {
      assertEquals(validationHelperunderTest.checkEmail(rightEmail), true);
    }
    for (String wrongEmail : wrongEmails) {
      assertEquals(validationHelperunderTest.checkEmail(wrongEmail), false);
    }
  }

  @Test
  public void checkPhoneIsCorrect () {
    //Given
    List<String> rightPhones = Arrays.asList(new String[]{"123456789", "1237872387"});
    List<String> wrongPhones = Arrays.asList(new String[]{"", "ads567567d", "1234567"});

    for (String rightPhone : rightPhones) {
      assertEquals(validationHelperunderTest.checkPhone(rightPhone), true);
    }
    for (String wrongPhone : wrongPhones) {
      assertEquals(validationHelperunderTest.checkPhone(wrongPhone), false);
    }
  }

  @Test
  public void checkNameIsCorrect () {
    //Given
    List<String> rightNames = Arrays.asList(new String[]{"Patrick", "Stefan", "Basti"});
    List<String> wrongNames = Arrays.asList(new String[]{"", "sd", "Patrick%&$", "Basti23"});

    for (String rightName : rightNames) {
      assertEquals(validationHelperunderTest.checkName(rightName), true);
    }
    for (String wrongName : wrongNames) {
      assertEquals(validationHelperunderTest.checkName(wrongName), false);
    }
  }

  @Test
  public void checkSurnameIsCorrect () {
    //Given
    List<String> rightSurnames = Arrays.asList(new String[]{"Mustermann", "Musterfrau"});
    List<String> wrongSurnames = Arrays.asList(new String[]{"", "sd", "Muster%&$", "Muster23"});

    for (String rightSurname : rightSurnames) {
      assertEquals(validationHelperunderTest.checkSurname(rightSurname), true);
    }
    for (String wrongSurname : wrongSurnames) {
      assertEquals(validationHelperunderTest.checkSurname(wrongSurname), false);
    }
  }
}
