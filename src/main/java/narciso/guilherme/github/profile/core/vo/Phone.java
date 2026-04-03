package narciso.guilherme.github.profile.core.vo;

public class Phone {
  private final String value;

  public Phone(String value) throws IllegalAccessException {
    validatePhoneNumber(value);
    this.value = value;
  }

  private void validatePhoneNumber(String value) throws IllegalAccessException {
    if(value.isBlank()) { // TODO: Add more validations later
      throw new IllegalAccessException("");
    }
  }

  private String sanatizePhone(String value) {
    return value; // TODO: Implement it later
  }

  public String getValue() {
    return value;
  }

  public String getMasked() {
    return value; // TODO: Implement it later
  }
}
