package narciso.guilherme.github.profile.input.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import narciso.guilherme.github.profile.core.entity.User;

import java.util.UUID;

@Schema(description = "User data returned by the API")
public record UserResponse(
    @Schema(description = "User unique identifier") UUID id,
    @Schema(description = "User's full name") String name,
    @Schema(description = "User's phone number") String phone,
    @Schema(description = "User's email address") String email,
    @Schema(description = "URL of the profile picture") String profilePictureUrl) {

  public static UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getPhone().getMasked(),
        user.getEmail().value(),
        user.getProfilePictureUrl());
  }
}
