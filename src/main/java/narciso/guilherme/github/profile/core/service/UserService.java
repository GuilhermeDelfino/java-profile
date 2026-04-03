package narciso.guilherme.github.profile.core.service;

import narciso.guilherme.github.profile.core.entity.User;
import narciso.guilherme.github.profile.core.output.SaveImage;
import narciso.guilherme.github.profile.core.output.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class UserService {

  private final UserRepository userRepository;
  private final SaveImage saveImage;

  public UserService(UserRepository userRepository, SaveImage saveImage) {
    this.userRepository = userRepository;
    this.saveImage = saveImage;
  }

  public User save(User user) {
    return user;
  }

  public Optional<User> findById(UUID id) {
    return Optional.empty();
  }

  public Collection<User> findAllPageable() {
    return Collections.emptyList();
  }

}
