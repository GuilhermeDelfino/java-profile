package narciso.guilherme.github.profile.core.service;

import narciso.guilherme.github.profile.core.entity.User;
import narciso.guilherme.github.profile.core.output.SaveImage;
import narciso.guilherme.github.profile.core.output.UserRepository;
import narciso.guilherme.github.profile.core.vo.Email;
import narciso.guilherme.github.profile.core.vo.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final SaveImage saveImage;

  public UserService(UserRepository userRepository, SaveImage saveImage) {
    this.userRepository = userRepository;
    this.saveImage = saveImage;
  }

  @Transactional
  public User save(
    String name,
    String phone,
    String email,
    String encodedPassword,
    InputStream image,
    long imageContentLength,
    String imageContentType
  ) {

    if (userRepository.existsByEmail(email)) {
      log.warn("Attempt to create user with email already in use: {}", email);
      throw new IllegalStateException("Email already in use: " + email);
    }
    Phone phoneVo = new Phone(phone);
    Email emailVo = new Email(email);
    String urlProfilePicture = saveImage.save(image, imageContentLength, imageContentType);
    User user = new User(name, phoneVo, urlProfilePicture, emailVo, encodedPassword);
    userRepository.createUser(user);
    log.info("User created: id={}, email={}", user.getId(), email);
    return user;
  }

  public Optional<User> findById(UUID id) {
    Optional<User> result = userRepository.findUser(id);
    if (result.isEmpty()) {
      log.warn("User not found: id={}", id);
    }
    return result;
  }

  public Collection<User> findAllPageable(int page, int size) throws IllegalArgumentException {
    if (page < 0 || size <= 0) {
      log.warn("Invalid pagination params: page={}, size={}", page, size);
      throw new IllegalArgumentException("Page and size must be greater than 0");
    }
    Collection<User> result = userRepository.findAllPaginated(page, size);
    log.info("Listed {} users: page={}, size={}", result.size(), page, size);
    return result;
  }

}
