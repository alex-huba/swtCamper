package swtcamper.api.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.IBookingController;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.exceptions.GenericServiceException;

public class BookingController implements IBookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserController userController;

    @Override
    public BookingDTO create(
            User user,
            Offer offer,
            LocalDate startDate,
            LocalDate endDate,
            boolean active
    ) {
        return modelMapper.bookingToBookingDTO(
                bookingService.create(user, offer, startDate, endDate)
        );
    }

    @Override
    public BookingDTO update(
            Long bookingID,
            LocalDate startDate,
            LocalDate endDate,
            boolean active
    ) throws GenericServiceException {
        try {
            return modelMapper.bookingToBookingDTO(
                    bookingService.update(bookingID, startDate, endDate, active, modelMapper.userToUserDTO(userController.getLoggedInUser()))
            );
        } catch (GenericServiceException e) {
            throw new GenericServiceException(e.getMessage());
        }
    }

    @Override
    public BookingDTO deactivate(Long bookingID) throws GenericServiceException {
        try {
            return modelMapper.bookingToBookingDTO(
                    bookingService.deactivate(bookingID, modelMapper.userToUserDTO(userController.getLoggedInUser()))
            );
        } catch (GenericServiceException e) {
            throw new GenericServiceException(e.getMessage());
        }
    }
}
