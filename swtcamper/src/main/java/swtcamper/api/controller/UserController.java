package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IUserController;
import swtcamper.backend.services.UserService;

public class UserController implements IUserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;


}
