package narciso.guilherme.github.profile.core.service;

import narciso.guilherme.github.profile.core.entity.User;
import narciso.guilherme.github.profile.core.output.SaveImage;
import narciso.guilherme.github.profile.core.output.UserRepository;
import narciso.guilherme.github.profile.core.vo.Email;
import narciso.guilherme.github.profile.core.vo.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private SaveImage saveImage;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, saveImage);
  }

  @Test
  void shouldSaveUserSuccessfully() {
    InputStream image = InputStream.nullInputStream();
    when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
    when(saveImage.save(any(), anyLong(), anyString())).thenReturn("https://bucket/img.jpg");

    User saved = userService.save("Alice", "11987654321", "user@example.com", "encoded", image, 100L, "image/jpeg");

    assertNotNull(saved.getId());
    assertEquals("Alice", saved.getName());
    assertEquals("user@example.com", saved.getEmail().value());
    verify(userRepository).createUser(saved);
  }

  @Test
  void shouldThrowWhenEmailAlreadyInUse() {
    when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

    assertThrows(IllegalStateException.class, () ->
        userService.save("Alice", "11987654321", "user@example.com", "encoded", InputStream.nullInputStream(), 100L, "image/jpeg")
    );

    verify(userRepository, never()).createUser(any());
  }

  @Test
  void shouldReturnUserById() {
    UUID id = UUID.randomUUID();
    User user = new User(id, "Alice", new Phone("11987654321"),
        "https://bucket/img.jpg", new Email("user@example.com"), "encoded");
    when(userRepository.findUser(id)).thenReturn(Optional.of(user));

    Optional<User> result = userService.findById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
  }

  @Test
  void shouldReturnEmptyWhenUserNotFound() {
    UUID id = UUID.randomUUID();
    when(userRepository.findUser(id)).thenReturn(Optional.empty());

    Optional<User> result = userService.findById(id);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnPagedUsers() {
    List<User> users = List.of(
        new User(UUID.randomUUID(), "Alice", new Phone("11987654321"),
            "https://bucket/img.jpg", new Email("alice@example.com"), "encoded")
    );
    when(userRepository.findAllPaginated(0, 10)).thenReturn(users);

    Collection<User> result = userService.findAllPageable(0, 10);

    assertEquals(1, result.size());
  }

  @Test
  void shouldThrowWhenPageIsNegative() {
    assertThrows(IllegalArgumentException.class, () -> userService.findAllPageable(-1, 10));
  }

  @Test
  void shouldThrowWhenSizeIsZero() {
    assertThrows(IllegalArgumentException.class, () -> userService.findAllPageable(0, 0));
  }
}
