package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.javafx.controller.MainViewController;

import java.time.LocalDate;
import java.util.List;

@Component
public class ValidationHelper {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferService offerService;

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
    return (
      !toCheck.isEmpty() && isNumber(toCheck) && Integer.parseInt(toCheck) > 0
    );
  }

  public static boolean checkOfferLocation(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  public static boolean checkOfferContact(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  /**
   * Checks if startDate is before endDate or on the same day
   * @param startDate
   * @param endDate
   * @return true if startDate is before endDate and not on the same day
   */
  public static boolean checkRentingDates(
    LocalDate startDate,
    LocalDate endDate
  ) {
    return startDate.isBefore(endDate) && !startDate.equals(endDate);
  }

  /**
   *  Checks if booked days of an offer lie between a startDate and an endDate
   * @param startDate
   * @param endDate
   * @param offer
   * @return true, if no booked days lie in between, false if booked days lie in between
   */
  public static boolean checkRentingDatesWithOffer(
    LocalDate startDate,
    LocalDate endDate,
    OfferDTO offer,
    // TODO checken
    BookingService bookingService,
    OfferService offerService,
    MainViewController mainViewController
  ) {
    boolean noBookedDaysInBetween = true;
    if (!checkRentingDates(startDate, endDate)) {
      noBookedDaysInBetween = false;
    } else {
      try {
        List<LocalDate> bookedDays = bookingService.getBookedDays(
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
    return noBookedDaysInBetween;
  }
}
