package swtcamper.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;
}
