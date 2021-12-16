package swtcamper.api.contract;

import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;

public interface IBookingController {

    public void create(BookingDTO bookingDTO) throws UserDoesNotExistException, GenericServiceException;

    public void update(BookingDTO bookingDTO) throws GenericServiceException;

    /**
     * Updates an offer by setting active = false.
     * @param bookingDTO
     */
    public void deactivate(BookingDTO bookingDTO) throws GenericServiceException;
}
