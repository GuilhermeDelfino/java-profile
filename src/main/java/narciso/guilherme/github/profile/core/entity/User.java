package narciso.guilherme.github.profile.core.entity;

import narciso.guilherme.github.profile.core.vo.Email;
import narciso.guilherme.github.profile.core.vo.Phone;

import java.util.UUID;

public class User {
  private final UUID id;
  private final String name;
  private final Phone phone;
  private final String profilePictureUrl;
  private final Email email;

  public User(String name, Phone phone, String profilePictureUrl, Email email) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.phone = phone;
    this.profilePictureUrl = profilePictureUrl;
    this.email = email;
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
}
