package narciso.guilherme.github.profile.core.vo;

public class Email {
  private final String value;

  public Email(String value) throws IllegalAccessException {
    validateEmail(value);
    this.value = value;
  }

  private void validateEmail(String value) throws IllegalAccessException {
    if(value.isBlank()) { // TODO: Add more validations later
      throw new IllegalAccessException("");
    }
  }

  public String getValue() {
    return value;
  }
}
