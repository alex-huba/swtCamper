package swtcamper.api.controller;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class ValidationHelper {

  public boolean lengthOverFive(String toCheck) {
    return toCheck.length() >= 5;
  }

  public boolean lengthOverThree(String toCheck) {
    return toCheck.length() >= 3;
  }

  public boolean isNumber(String toCheck) {
    return toCheck.matches("[0-9]*");
  }

  public boolean checkOfferTitle(String toCheck) {
    return !toCheck.isEmpty() && lengthOverFive(toCheck);
  }

  public boolean checkOfferPrice(String toCheck) {
    return (!toCheck.isEmpty() && isNumber(toCheck) && Integer.parseInt(toCheck) > 0);
  }

  public boolean checkOfferLocation(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  public boolean checkOfferContact(String toCheck) {
    return !toCheck.isEmpty() && lengthOverFive(toCheck);
  }

  public boolean checkRentingDate(LocalDate startDate, LocalDate endDate) {
    return !startDate.isBefore(endDate);
  }
}
