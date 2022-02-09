package swtcamper.backend.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.api.controller.ValidationHelper;

@RunWith(MockitoJUnitRunner.class)
public class ValidationHelperUnitTest {

  @InjectMocks
  private ValidationHelper validationHelperunderTest;

  @Test
  public void checkOfferTitleIsCorrect() {
    //Given
    List<String> rightTitles = Arrays.asList("good Title", "another Title");
    List<String> wrongTitles = Arrays.asList(
      "",
      "5sd6",
      "dfg",
      "Title with &%§<<#"
    );

    for (String rightTitle : rightTitles) {
      assertEquals(ValidationHelper.checkOfferTitle(rightTitle), true);
    }
    for (String wrongTitle : wrongTitles) {
      assertEquals(ValidationHelper.checkOfferTitle(wrongTitle), false);
    }
  }

  @Test
  public void checkOfferPriceIsCorrect() {
    //Given
    List<String> rightPrices = Arrays.asList("11", "233", "45", "10000");
    List<String> wrongPrices = Arrays.asList("", "3", "dfg", "345353");

    for (String rightPrice : rightPrices) {
      assertEquals(ValidationHelper.checkOfferPrice(rightPrice), true);
    }
    for (String wrongPrice : wrongPrices) {
      assertEquals(ValidationHelper.checkOfferPrice(wrongPrice), false);
    }
  }

  @Test
  public void checkOfferLocationIsCorrect() {
    //Given
    List<String> rightLocations = Arrays.asList("Bamberg", "Nürnberg");
    List<String> wrongLocations = Arrays.asList("", "ad", "12");

    for (String rightLocation : rightLocations) {
      assertEquals(ValidationHelper.checkOfferLocation(rightLocation), true);
    }
    for (String wrongLocation : wrongLocations) {
      assertEquals(ValidationHelper.checkOfferLocation(wrongLocation), false);
    }
  }

  @Test
  public void checkOfferContactIsCorrect() {
    //Given
    List<String> rightLocations = Arrays.asList("32452345", "2345345");
    List<String> wrongLocations = Arrays.asList("", "ad", "12");

    for (String rightLocation : rightLocations) {
      assertEquals(ValidationHelper.checkOfferContact(rightLocation), true);
    }
    for (String wrongLocation : wrongLocations) {
      assertEquals(ValidationHelper.checkOfferContact(wrongLocation), false);
    }
  }

  @Test
  public void checkUsernameIsCorrect() {
    //Given
    List<String> rightUsernames = Arrays.asList("Patrick", "Stefan1201");
    List<String> wrongUsernames = Arrays.asList("", "adsd", "Patrick%&$");

    for (String rightUsername : rightUsernames) {
      assertEquals(ValidationHelper.checkUserName(rightUsername), true);
    }
    for (String wrongUsername : wrongUsernames) {
      assertEquals(ValidationHelper.checkUserName(wrongUsername), false);
    }
  }

  @Test
  public void checkPasswordIsCorrect() {
    //Given
    List<String> rightPasswords = Arrays.asList("password-12", "test12$%");
    List<String> wrongPasswords = Arrays.asList("", "23ad", "password");

    for (String rightPassword : rightPasswords) {
      assertEquals(ValidationHelper.checkPassword(rightPassword), true);
    }
    for (String wrongPassword : wrongPasswords) {
      assertEquals(ValidationHelper.checkPassword(wrongPassword), false);
    }
  }

  @Test
  public void checkEmailIsCorrect() {
    //Given
    List<String> rightEmails = Arrays.asList(
      "test@mail.de",
      "test.test@mail.com"
    );
    List<String> wrongEmails = Arrays.asList(
      "",
      "adsd",
      "test@mail",
      "test.de"
    );

    for (String rightEmail : rightEmails) {
      assertEquals(ValidationHelper.checkEmail(rightEmail), true);
    }
    for (String wrongEmail : wrongEmails) {
      assertEquals(ValidationHelper.checkEmail(wrongEmail), false);
    }
  }

  @Test
  public void checkPhoneIsCorrect() {
    //Given
    List<String> rightPhones = Arrays.asList("123456789", "1237872387");
    List<String> wrongPhones = Arrays.asList("", "ads567567d", "1234567");

    for (String rightPhone : rightPhones) {
      assertEquals(ValidationHelper.checkPhone(rightPhone), true);
    }
    for (String wrongPhone : wrongPhones) {
      assertEquals(ValidationHelper.checkPhone(wrongPhone), false);
    }
  }

  @Test
  public void checkNameIsCorrect() {
    //Given
    List<String> rightNames = Arrays.asList("Patrick", "Stefan", "Basti");
    List<String> wrongNames = Arrays.asList("", "sd", "Patrick%&$", "Basti23");

    for (String rightName : rightNames) {
      assertEquals(ValidationHelper.checkName(rightName), true);
    }
    for (String wrongName : wrongNames) {
      assertEquals(ValidationHelper.checkName(wrongName), false);
    }
  }

  @Test
  public void checkSurnameIsCorrect() {
    //Given
    List<String> rightSurnames = Arrays.asList("Mustermann", "Musterfrau");
    List<String> wrongSurnames = Arrays.asList(
      "",
      "sd",
      "Muster%&$",
      "Muster23"
    );

    for (String rightSurname : rightSurnames) {
      assertEquals(ValidationHelper.checkSurname(rightSurname), true);
    }
    for (String wrongSurname : wrongSurnames) {
      assertEquals(ValidationHelper.checkSurname(wrongSurname), false);
    }
  }
}
