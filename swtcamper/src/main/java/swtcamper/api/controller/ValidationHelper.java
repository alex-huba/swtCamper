package swtcamper.api.controller;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * A helper class for input validation.
 */
@Component
public class ValidationHelper {

  public static boolean lengthOverFive(String toCheck) {
    return toCheck.length() >= 5;
  }

  public static boolean lengthOverThree(String toCheck) {
    return toCheck.length() >= 3;
  }

  public static boolean isNumber(String toCheck) {
    return toCheck.matches("[0-9]*");
  }

  public static boolean checkOfferTitle(String toCheck) {
    return !toCheck.isEmpty() && lengthOverFive(toCheck);
  }

  public static boolean checkOfferPrice(String toCheck) {
    return (!toCheck.isEmpty() && isNumber(toCheck) && Integer.parseInt(toCheck) > 0);
  }

  public static boolean checkOfferLocation(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  public static boolean checkOfferContact(String toCheck) {
    return !toCheck.isEmpty() && lengthOverFive(toCheck);
  }

  public static boolean checkRentingDate(LocalDate startDate, LocalDate endDate) {
    return !startDate.isBefore(endDate);
  }
}
