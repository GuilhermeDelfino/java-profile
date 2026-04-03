package narciso.guilherme.github.profile.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    // 32-byte key encoded in Base64 (minimum for HMAC-SHA256)
    String secret = Base64.getEncoder().encodeToString("test-secret-key-32-bytes-long!!!".getBytes());
    jwtService = new JwtService(secret, 60_000L);
  }

  @Test
  void shouldGenerateToken() {
    String token = jwtService.generateToken("user@example.com");
    assertNotNull(token);
    assertFalse(token.isBlank());
  }

  @Test
  void shouldExtractSubjectFromToken() {
    String token = jwtService.generateToken("user@example.com");
    assertEquals("user@example.com", jwtService.extractSubject(token));
  }

  @Test
  void shouldReturnTrueForValidToken() {
    String token = jwtService.generateToken("user@example.com");
    assertTrue(jwtService.isValid(token));
  }

  @Test
  void shouldReturnFalseForTamperedToken() {
    String token = jwtService.generateToken("user@example.com");
    String tampered = token.substring(0, token.length() - 4) + "xxxx";
    assertFalse(jwtService.isValid(tampered));
  }

  @Test
  void shouldReturnFalseForExpiredToken() throws InterruptedException {
    String secret = Base64.getEncoder().encodeToString("test-secret-key-32-bytes-long!!!".getBytes());
    JwtService shortLivedService = new JwtService(secret, 1L);
    String token = shortLivedService.generateToken("user@example.com");
    Thread.sleep(10);
    assertFalse(shortLivedService.isValid(token));
  }
}
