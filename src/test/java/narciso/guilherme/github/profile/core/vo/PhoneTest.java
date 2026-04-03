package narciso.guilherme.github.profile.core.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

  @Test
  void shouldCreatePhoneWith10Digits() {
    Phone phone = new Phone("1132334455");
    assertEquals("1132334455", phone.getValue());
  }

  @Test
  void shouldCreatePhoneWith11Digits() {
    Phone phone = new Phone("11987654321");
    assertEquals("11987654321", phone.getValue());
  }

  @Test
  void shouldSanitizeMaskCharsBeforeValidating() {
    Phone phone = new Phone("(11) 9876-54321");
    assertEquals("11987654321", phone.getValue());
  }

  @Test
  void shouldThrowWhenPhoneHasTooFewDigits() {
    assertThrows(IllegalArgumentException.class, () -> new Phone("123456789"));
  }

  @Test
  void shouldThrowWhenPhoneHasTooManyDigits() {
    assertThrows(IllegalArgumentException.class, () -> new Phone("123456789012"));
  }

  @Test
  void shouldReturnMaskedFormatFor11Digits() {
    Phone phone = new Phone("11987654321");
    assertEquals("(11) 98765-4321", phone.getMasked());
  }

  @Test
  void shouldReturnMaskedFormatFor10Digits() {
    Phone phone = new Phone("1132334455");
    assertEquals("(11) 3233-4455", phone.getMasked());
  }
}
