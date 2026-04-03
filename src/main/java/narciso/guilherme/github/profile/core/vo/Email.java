package narciso.guilherme.github.profile.core.vo;

public record Email(String value) {

  private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

  public Email {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Email must not be blank");
    }
    if (!value.matches(EMAIL_REGEX)) {
      throw new IllegalArgumentException("Invalid email format: " + value);
    }
  }
}
