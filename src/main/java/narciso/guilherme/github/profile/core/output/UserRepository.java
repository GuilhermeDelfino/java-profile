package narciso.guilherme.github.profile.core.output;

import narciso.guilherme.github.profile.core.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  User createUser(User user);
  Optional<User> findUser(UUID id);
  Collection<User> findAll();
//  Collection<User> findAllPaginated(); // TODO:
}
