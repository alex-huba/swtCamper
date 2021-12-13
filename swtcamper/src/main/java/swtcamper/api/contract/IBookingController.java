package swtcamper.api.contract;

import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;

import java.time.LocalDate;

public interface IBookingController {

    public void create(
            User user,
            Offer offer,
            LocalDate startDate,
            LocalDate endDate,
            boolean active
    ) throws UserDoesNotExistException, GenericServiceException;

    public void update(
            Long offerID,
            LocalDate startDate,
            LocalDate endDate,
            Boolean active
    ) throws GenericServiceException;

    /**
     * Updates an offer by setting active = false.
     * @param offerID
     */
    public void deactivate(
            Long offerID
    ) throws GenericServiceException;
}
