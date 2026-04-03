package narciso.guilherme.github.profile.output.database;

import narciso.guilherme.github.profile.core.entity.User;
import narciso.guilherme.github.profile.core.vo.Email;
import narciso.guilherme.github.profile.core.vo.Phone;

public class UserMapper {

  private UserMapper() {}

  public static User toDomain(UserModel model) {
    return new User(
        model.getId(),
        model.getName(),
        new Phone(model.getPhone()),
        model.getProfilePictureUrl(),
        new Email(model.getEmail()),
        model.getPassword());
  }

  public static UserModel toModel(User user) {
    return new UserModel(
        user.getId(),
        user.getName(),
        user.getPhone().getValue(),
        user.getEmail().value(),
        user.getProfilePictureUrl(),
        user.getPassword());
  }
}
