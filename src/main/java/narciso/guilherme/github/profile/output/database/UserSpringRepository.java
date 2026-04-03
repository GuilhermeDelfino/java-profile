package narciso.guilherme.github.profile.output.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSpringRepository extends JpaRepository<UserModel, UUID> {
  Optional<UserModel> findByEmail(String email);
  boolean existsByEmail(String email);
}
