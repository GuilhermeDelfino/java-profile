package narciso.guilherme.github.profile.core.entity;

import narciso.guilherme.github.profile.core.vo.Email;
import narciso.guilherme.github.profile.core.vo.Phone;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final String name;
  private final Phone phone;
  private final String profilePictureUrl;
  private final Email email;
  private final String password;

  public User(String name, Phone phone, String profilePictureUrl, Email email, String password) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.phone = phone;
    this.profilePictureUrl = profilePictureUrl;
    this.email = email;
    this.password = password;
  }

  public User(UUID id, String name, Phone phone, String profilePictureUrl, Email email, String password) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.profilePictureUrl = profilePictureUrl;
    this.email = email;
    this.password = password;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Phone getPhone() {
    return phone;
  }

  public String getProfilePictureUrl() {
    return profilePictureUrl;
  }

  public Email getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
