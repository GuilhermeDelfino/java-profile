package narciso.guilherme.github.profile.output.database;

import narciso.guilherme.github.profile.core.entity.User;
import narciso.guilherme.github.profile.core.output.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final UserSpringRepository userSpringRepository;

  public UserRepositoryImpl(UserSpringRepository userSpringRepository) {
    this.userSpringRepository = userSpringRepository;
  }

  @Override
  public void createUser(User user) {
    userSpringRepository.save(UserMapper.toModel(user));
  }

  @Override
  public boolean existsByEmail(String email) {
    return userSpringRepository.existsByEmail(email);
  }

  @Override
  public Optional<User> findUser(UUID id) {
    return userSpringRepository.findById(id).map(UserMapper::toDomain);
  }

  @Override
  public Collection<User> findAllPaginated(int page, int size) {
    return userSpringRepository.findAll(PageRequest.of(page, size)).stream()
      .map(UserMapper::toDomain)
      .toList();
  }
}
