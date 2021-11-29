package swtcamper.api.contract;

import swtcamper.backend.entities.UserRole;

public class UserDTO {

  private Long id;
  private String username;
  private String name;
  private String surname;
  private String email;
  private String phone;
  private String password;
  private UserRole userRole;

  private boolean locked;
  private boolean enabled;

  public UserDTO(
    Long id,
    String username,
    String name,
    String surname,
    String email,
    String phone,
    String password,
    UserRole userRole,
    boolean locked,
    boolean enabled
  ) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.userRole = userRole;
    this.locked = locked;
    this.enabled = enabled;
  }

  public UserDTO(
    String username,
    String name,
    String surname,
    String email,
    String phone,
    String password,
    UserRole userRole,
    boolean locked,
    boolean enabled
  ) {
    this.username = username;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.userRole = userRole;
    this.locked = locked;
    this.enabled = enabled;
  }

  public UserDTO() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
