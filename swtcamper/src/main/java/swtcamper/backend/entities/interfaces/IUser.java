package swtcamper.backend.entities.interfaces;

import java.util.ArrayList;
import swtcamper.backend.entities.UserRole;

public interface IUser {
  Long getId();

  void setId(Long id);

  String getUsername();

  void setUsername(String username);

  String getName();

  void setName(String name);

  String getSurname();

  void setSurname(String surname);

  String getEmail();

  void setEmail(String email);

  String getPhone();

  void setPhone(String phone);

  String getPassword();

  void setPassword(String password);

  UserRole getUserRole();

  void setUserRole(UserRole userRole);

  boolean isLocked();

  void setLocked(boolean locked);

  boolean isEnabled();

  void setEnabled(boolean enabled);

  ArrayList<Long> getExcludedRenters();

  void setExcludedRenters(ArrayList<Long> excludedRenters);
}
