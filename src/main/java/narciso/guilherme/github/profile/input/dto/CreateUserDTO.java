package narciso.guilherme.github.profile.input.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Payload to create a new user")
public class CreateUserDTO {

  @NotBlank
  @Schema(description = "User full name")
  private String name;

  @NotBlank
  @Schema(description = "User phone number")
  private String phone;

  @NotBlank
  @Email
  @Schema(description = "User email")
  private String email;

  @NotBlank
  @Schema(description = "User password")
  private String password;

  @Schema(description = "Profile picture file")
  private MultipartFile image;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public MultipartFile getImage() {
    return image;
  }

  public void setImage(MultipartFile image) {
    this.image = image;
  }
}
