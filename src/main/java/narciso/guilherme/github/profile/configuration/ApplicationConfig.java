package narciso.guilherme.github.profile.configuration;

import narciso.guilherme.github.profile.core.output.SaveImage;
import narciso.guilherme.github.profile.core.output.UserRepository;
import narciso.guilherme.github.profile.core.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

  @Bean
  public UserService userService(SaveImage saveImage, UserRepository userRepository) {
    return new UserService(userRepository, saveImage);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
