package narciso.guilherme.github.profile.output.database;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserSpringRepository userSpringRepository;

  public UserDetailsServiceImpl(UserSpringRepository userSpringRepository) {
    this.userSpringRepository = userSpringRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserModel model = userSpringRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

    return new User(model.getEmail(), model.getPassword(), List.of());
  }
}
