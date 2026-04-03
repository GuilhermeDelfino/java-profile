package narciso.guilherme.github.profile.output.database;

import narciso.guilherme.github.profile.core.entity.User;
import narciso.guilherme.github.profile.core.output.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final UserSpringRepository userSpringRepository;

  public UserRepositoryImpl(UserSpringRepository userSpringRepository) {
    this.userSpringRepository = userSpringRepository;
  }

  @Override
  public User createUser(User user) {
    return null;
  }

  @Override
  public Optional<User> findUser(UUID id) {
    return Optional.empty();
  }

  @Override
  public Collection<User> findAll() {
    return List.of();
  }
}
