package swtcamper.api.contract;

import swtcamper.backend.services.exceptions.GenericServiceException;

public interface IUserController {

    public UserDTO register(UserDTO userDTO) throws GenericServiceException;

    public void login(String username, String password) throws GenericServiceException;

}
