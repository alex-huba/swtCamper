package swtcamper.backend.entities;

import java.util.ArrayList;
import javax.persistence.*;
import swtcamper.backend.entities.interfaces.IUser;

@Entity
public class User implements IUser {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  private String name;
  private String surname;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String phone;

  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  private boolean locked;
  private boolean enabled;

  private ArrayList<Long> excludedRenters;

  public User() {
    super();
    this.locked = false;
    this.enabled = false;
  }

  public User(
    String username,
    String password,
    String email,
    String phone,
    String name,
    String surname,
    UserRole userRole,
    boolean enabled
  ) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.phone = phone;
    this.name = name;
    this.surname = surname;
    this.userRole = userRole;
    this.enabled = enabled;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getSurname() {
    return surname;
  }

  @Override
  public void setSurname(String surname) {
    this.surname = surname;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPhone() {
    return phone;
  }

  @Override
  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public UserRole getUserRole() {
    return userRole;
  }

  @Override
  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  @Override
  public boolean isLocked() {
    return locked;
  }

  @Override
  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public ArrayList<Long> getExcludedRenters() {
    return this.excludedRenters;
  }

  @Override
  public void setExcludedRenters(ArrayList<Long> excludedRenters) {
    this.excludedRenters = excludedRenters;
  }

  @Override
  public String toString() {
    return (
      String.format(
        "%s (%s %s), %s, Befugnis: %s, %s, %s",
        username,
        name,
        surname,
        email,
        userRole,
        enabled ? "akzeptiert" : "ignoriert",
        locked ? "geblockt" : "entblockt"
      )
    );
  }
}
