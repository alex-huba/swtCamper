package swtcamper.backend.entities;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class User {

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

  public ArrayList<Long> getExcludedRenters() {
    return this.excludedRenters;
  }

  public void setExcludedRenters(ArrayList<Long> excludedRenters) {
    this.excludedRenters = excludedRenters;
  }
}
