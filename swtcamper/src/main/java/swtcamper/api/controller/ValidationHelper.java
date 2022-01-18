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
    return (
      !toCheck.isEmpty() &&
      isNumber(toCheck) &&
      Integer.parseInt(toCheck) > 10 &&
      Integer.parseInt(toCheck) < 100001
    );
  }

  public boolean checkOfferLocation(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  public boolean checkOfferContact(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  public boolean checkRentingDate(LocalDate startDate, LocalDate endDate) {
    return !startDate.isBefore(endDate);
  }

  public boolean checkSizeParameter(int toCheck) {
    return (toCheck > 99 && toCheck < 100001);
  }

  public boolean checkYear(int toCheck) {
    return (toCheck > 1907 && toCheck < 2023);
  }

  public boolean checkSeats(String toCheck) {
    return (
      !toCheck.isEmpty() &&
      isNumber(toCheck) &&
      Integer.parseInt(toCheck) > -1 &&
      Integer.parseInt(toCheck) < 13
    );
  }

  public boolean checkBeds(String toCheck) {
    return (
      !toCheck.isEmpty() &&
      isNumber(toCheck) &&
      Integer.parseInt(toCheck) > -1 &&
      Integer.parseInt(toCheck) < 13
    );
  }
}
