package narciso.guilherme.github.profile.configuration;

import narciso.guilherme.github.profile.core.service.UserService;
import narciso.guilherme.github.profile.output.aws.S3SaveImage;
import narciso.guilherme.github.profile.output.database.UserRepositoryImpl;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  public UserService userService(
    S3SaveImage s3SaveImage,
    UserRepositoryImpl userRepository
  ){
    return new UserService(userRepository, s3SaveImage);
  }
}
