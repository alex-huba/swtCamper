package swtcamper.backend.entities;

import javax.persistence.*;

@Entity
public class User {

  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  private String name;
  private String surname;
  private String email;
  private String phone;
  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  private boolean locked;
  private boolean enabled;

  public User() {
    super();
    this.locked = false;
    this.enabled = false;
  }

  public User(String username, String password, String email, String phone, String name, String surname, UserRole userRole, boolean enabled) {
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
  public String toString() {
    return (
      "User{" +
      "id=" +
      id +
      ", username='" +
      username +
      '\'' +
      ", name='" +
      name +
      '\'' +
      ", surname='" +
      surname +
      '\'' +
      ", email='" +
      email +
      '\'' +
      ", phone='" +
      phone +
      '\'' +
      ", password='" +
      password +
      '\'' +
      ", userRole=" +
      userRole +
      ", locked=" +
      locked +
      ", enabled=" +
      enabled +
      '}'
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
}
