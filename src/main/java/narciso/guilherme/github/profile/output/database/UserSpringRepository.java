package narciso.guilherme.github.profile.output.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSpringRepository extends JpaRepository<UUID, UserModel> {
}
