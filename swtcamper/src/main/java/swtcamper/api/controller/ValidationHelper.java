package swtcamper.api.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.javafx.controller.MainViewController;

@Component
public class ValidationHelper {

  public static boolean checkStringLength(String toCheck, int min, int max) {
    return (min <= toCheck.length() && toCheck.length() <= max);
  }

  public static boolean isNumber(String toCheck) {
    return toCheck.matches("[0-9]*");
  }

  public static boolean isLetters(String toCheck) {
    return toCheck.matches("^[a-zA-ZäöüÄÖÜ]*");
  }

  public static boolean containsSpecialCharacters(String toCheck) {
    String specialCharacters = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
    for (int i = 0; i < toCheck.length(); i++) {
      if (specialCharacters.contains(Character.toString(toCheck.charAt(i)))) {
        return true;
      }
    }
    return false;
  }

  public static boolean containsNumbers(String toCheck) {
    String specialCharacters = "1234567890";
    for (int i = 0; i < toCheck.length(); i++) {
      if (specialCharacters.contains(Character.toString(toCheck.charAt(i)))) {
        return true;
      }
    }
    return false;
  }

  public static boolean noSpecialCharacters(String toCheck) {
    return toCheck.matches("^[a-zA-Z0-9.-äöüÄÖÜ ]*");
  }

  public static boolean checkOfferTitle(String toCheck) {
    return (checkStringLength(toCheck, 5, 999) && noSpecialCharacters(toCheck));
  }

  public static boolean checkOfferPrice(String toCheck) {
    return (
      !toCheck.isEmpty() &&
      isNumber(toCheck) &&
      Integer.parseInt(toCheck) > 10 &&
      Integer.parseInt(toCheck) < 100001
    );
  }

  public static boolean checkOfferLocation(String toCheck) {
    return (checkStringLength(toCheck, 3, 99));
  }

  public static boolean checkOfferContact(String toCheck) {
    return (checkStringLength(toCheck, 3, 99));
  }

  /**
   * Checks if startDate is before endDate or on the same day
   *
   * @param startDate
   * @param endDate
   * @return true if startDate is before endDate and not on the same day
   */
  public static boolean checkRentingDates(
    LocalDate startDate,
    LocalDate endDate
  ) {
    return !startDate.isBefore(endDate) || startDate.equals(endDate);
  }

  /**
   * Checks if booked days of an offer lie between a startDate and an endDate
   *
   * @param startDate
   * @param endDate
   * @param offer
   * @return true, if no booked days lie in between, false if booked days lie in between
   */
  public static boolean checkRentingDatesWithOffer(
    LocalDate startDate,
    LocalDate endDate,
    OfferDTO offer,
    BookingService bookingService,
    OfferService offerService,
    MainViewController mainViewController
  ) {
    boolean noBookedDaysInBetween = true;
    if (checkRentingDates(startDate, endDate)) {
      noBookedDaysInBetween = false;
    } else {
      try {
        List<LocalDate> bookedDays = bookingService.getBookedAndBlockedDays(
          offer.getID()
        );
        List<LocalDate> blockedDays = offerService.getBlockedDates(
          offer.getID()
        );
        for (LocalDate day : bookedDays) {
          if (day.isAfter(startDate) && day.isBefore(endDate)) {
            noBookedDaysInBetween = false;
          }
        }
        for (LocalDate day : blockedDays) {
          if (day.isAfter(startDate) && day.isBefore(endDate)) {
            noBookedDaysInBetween = false;
          }
        }
      } catch (GenericServiceException e) {
        mainViewController.handleExceptionMessage(e.getMessage());
      }
    }
    return !noBookedDaysInBetween;
  }

  public static boolean checkUserName(String toCheck) {
    return (checkStringLength(toCheck, 5, 25) && noSpecialCharacters(toCheck));
  }

  public static boolean checkPassword(String toCheck) {
    return (
      checkStringLength(toCheck, 5, 25) &&
      containsNumbers(toCheck) &&
      containsSpecialCharacters(toCheck)
    );
  }

  public static boolean checkEmail(String toCheck) {
    return toCheck.matches(
      "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );
  }

  public static boolean checkPhone(String toCheck) {
    return (isNumber(toCheck) && checkStringLength(toCheck, 8, 15));
  }

  public static boolean checkName(String toCheck) {
    return (isLetters(toCheck) && checkStringLength(toCheck, 3, 20));
  }

  public static boolean checkSurname(String toCheck) {
    return (isLetters(toCheck) && checkStringLength(toCheck, 3, 20));
  }

  public boolean checkSizeParameter(int toCheck) {
    return (toCheck > 99 && toCheck < 100001);
  }

  public boolean checkYear(int toCheck) {
    return (toCheck > 1907 && toCheck < LocalDate.now().getYear() + 1);
  }
}
