package swtcamper.api.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.javafx.controller.MainViewController;

@Component
public class ValidationHelper {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private MainViewController mainViewController;

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
      !toCheck.isEmpty() && isNumber(toCheck) && Integer.parseInt(toCheck) > 0
    );
  }

  public boolean checkOfferLocation(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  public boolean checkOfferContact(String toCheck) {
    return !toCheck.isEmpty() && lengthOverThree(toCheck);
  }

  public boolean checkRentingDates(LocalDate startDate, LocalDate endDate) {
    return startDate.isBefore(endDate) && !startDate.equals(endDate);
  }

  public boolean checkRentingDatesWithOffer(
    LocalDate startDate,
    LocalDate endDate,
    OfferDTO offer
  ) {
    boolean noBookedDaysInBetween = true;
    if (!checkRentingDates(startDate, endDate)) {
      noBookedDaysInBetween = false;
    } else {
      try {
        List<LocalDate> bookedDays = bookingService.getBookedDays(
          offer.getID()
        );
        for (LocalDate day : bookedDays) {
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
