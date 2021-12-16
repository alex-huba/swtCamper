package swtcamper.api.controller;

import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.IBookingController;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;

public class BookingController implements IBookingController {
    @Override
    public void create(BookingDTO bookingDTO) throws UserDoesNotExistException, GenericServiceException {
        // TODO: implement booking creation
    }

    @Override
    public void update(BookingDTO bookingDTO) throws GenericServiceException {
        // TODO: implement booking update
    }

    @Override
    public void deactivate(BookingDTO bookingDTO) throws GenericServiceException {
        // TODO: implement booking deactivation
    }
}
