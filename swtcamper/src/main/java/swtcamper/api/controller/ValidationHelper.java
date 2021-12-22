package swtcamper.api.controller;

import javafx.scene.control.DatePicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidationHelper {

    public boolean lengthOverFive (String toCheck) {
        if (toCheck.length() < 5) { return false; }
        else { return true; }
    }

    public boolean lengthOverThree (String toCheck) {
        if (toCheck.length() < 3) { return false; }
        else { return true; }
    }

    public boolean isNumber (String toCheck) {
        if (!toCheck.matches("[0-9]*")) { return false; }
        else { return true; }
    }

    public boolean checkOfferTitle (String toCheck) {
        if (toCheck.isEmpty() || !lengthOverFive(toCheck)) { return false; }
        else { return true; }
    }

    public boolean checkOfferPrice (String toCheck) {
        if (toCheck.isEmpty() || !isNumber(toCheck) || Integer.parseInt(toCheck) <= 0) { return false; }
        else { return true; }
    }

    public boolean checkOfferLocation (String toCheck) {
        if (toCheck.isEmpty() || !lengthOverThree(toCheck)) { return false; }
        else { return true; }
    }

    public boolean checkOfferContact (String toCheck) {
        if (toCheck.isEmpty() || !lengthOverThree(toCheck)) { return false; }
        else { return true; }
    }

    public boolean checkRentingDate (LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(endDate)) { return false; }
        else { return true; }
    }
}