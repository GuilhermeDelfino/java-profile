package narciso.guilherme.github.profile.core.output;

import narciso.guilherme.github.profile.core.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  void createUser(User user);
  boolean existsByEmail(String email);
  Optional<User> findUser(UUID id) throws IllegalArgumentException;
  Collection<User> findAllPaginated(int page, int size) throws IllegalArgumentException;
}
