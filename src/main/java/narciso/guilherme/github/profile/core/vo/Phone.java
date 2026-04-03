package narciso.guilherme.github.profile.core.vo;

public class Phone {

  private final String value;

  public Phone(String value) {
    String sanitized = sanitize(value);
    validate(sanitized);
    this.value = sanitized;
  }

  private String sanitize(String value) {
    return value.replaceAll("\\D", "");
  }

  private void validate(String value) {
    if (value.length() < 10 || value.length() > 11) {
      throw new IllegalArgumentException("Invalid phone number: must have 10 or 11 digits");
    }
  }

  public String getValue() {
    return value;
  }

  public String getMasked() {
    if (value.length() == 11) {
      return "(%s) %s-%s".formatted(value.substring(0, 2), value.substring(2, 7), value.substring(7));
    }
    return "(%s) %s-%s".formatted(value.substring(0, 2), value.substring(2, 6), value.substring(6));
  }
}
