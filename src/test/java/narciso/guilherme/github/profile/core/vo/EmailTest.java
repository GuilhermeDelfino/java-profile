package narciso.guilherme.github.profile.core.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

  @Test
  void shouldCreateValidEmail() {
    Email email = new Email("user@example.com");
    assertEquals("user@example.com", email.value());
  }

  @Test
  void shouldThrowWhenEmailIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Email(null));
  }

  @Test
  void shouldThrowWhenEmailIsBlank() {
    assertThrows(IllegalArgumentException.class, () -> new Email("   "));
  }

  @Test
  void shouldThrowWhenEmailHasNoAtSign() {
    assertThrows(IllegalArgumentException.class, () -> new Email("userexample.com"));
  }

  @Test
  void shouldThrowWhenEmailHasNoDomain() {
    assertThrows(IllegalArgumentException.class, () -> new Email("user@"));
  }

  @Test
  void shouldThrowWhenEmailHasSpaces() {
    assertThrows(IllegalArgumentException.class, () -> new Email("user @example.com"));
  }
}
