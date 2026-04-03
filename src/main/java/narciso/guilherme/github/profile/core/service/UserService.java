package narciso.guilherme.github.profile.core.service;

import narciso.guilherme.github.profile.core.entity.User;
import narciso.guilherme.github.profile.core.output.SaveImage;
import narciso.guilherme.github.profile.core.output.UserRepository;
import narciso.guilherme.github.profile.core.vo.Email;
import narciso.guilherme.github.profile.core.vo.Phone;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class UserService {

  private final UserRepository userRepository;
  private final SaveImage saveImage;

  public UserService(UserRepository userRepository, SaveImage saveImage) {
    this.userRepository = userRepository;
    this.saveImage = saveImage;
  }

  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "users", allEntries = true),
      @CacheEvict(value = "users-list", allEntries = true)
  })
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
      throw new IllegalStateException("Email already in use: " + email);
    }
    Phone phoneVo = new Phone(phone);
    Email emailVo = new Email(email);
    String urlProfilePicture = saveImage.save(image, imageContentLength, imageContentType);
    User user = new User(name, phoneVo, urlProfilePicture, emailVo, encodedPassword);
    userRepository.createUser(user);
    return user;
  }

  @Cacheable(value = "users", key = "#id")
  public Optional<User> findById(UUID id) {
    return userRepository.findUser(id);
  }

  @Cacheable(value = "users-list", key = "#page + '-' + #size")
  public Collection<User> findAllPageable(int page, int size) throws IllegalArgumentException {
    if (page < 0 || size <= 0) {
      throw new IllegalArgumentException("Page and size must be greater than 0");
    }
    return userRepository.findAllPaginated(page, size);
  }

}
