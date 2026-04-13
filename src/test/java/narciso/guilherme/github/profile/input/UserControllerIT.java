package narciso.guilherme.github.profile.input;

import narciso.guilherme.github.profile.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends AbstractIntegrationTest {

  @BeforeEach
  void setupSaveImageMock() {
    when(saveImage.save(any(), anyLong(), anyString())).thenReturn("https://s3.example.com/img.jpg");
  }

  @Test
  void shouldCreateUser() throws Exception {
    mockMvc.perform(multipart("/users")
            .file(new MockMultipartFile("image", "img.jpg", "image/jpeg", "fake".getBytes()))
            .param("name", "Alice")
            .param("phone", "11987654321")
            .param("email", "alice-create@example.com")
            .param("password", "password123"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value("Alice"))
        .andExpect(jsonPath("$.email").value("alice-create@example.com"));
  }

  @Test
  void shouldReturn409WhenEmailAlreadyExists() throws Exception {
    createUser("conflict@example.com");

    mockMvc.perform(multipart("/users")
            .file(new MockMultipartFile("image", "img.jpg", "image/jpeg", "fake".getBytes()))
            .param("name", "Bob")
            .param("phone", "11987654322")
            .param("email", "conflict@example.com")
            .param("password", "password123"))
        .andExpect(status().isConflict());
  }

  @Test
  void shouldFindUserById() throws Exception {
    String createResponse = mockMvc.perform(multipart("/users")
            .file(new MockMultipartFile("image", "img.jpg", "image/jpeg", "fake".getBytes()))
            .param("name", "Alice")
            .param("phone", "11987654321")
            .param("email", "findbyid@example.com")
            .param("password", "password123"))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    String id = objectMapper.readTree(createResponse).get("id").asText();
    String token = getToken("findbyid@example.com", "password123");

    mockMvc.perform(get("/users/" + id)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.email").value("findbyid@example.com"));

    mockMvc.perform(get("/users/" + id)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.email").value("findbyid@example.com"));
  }

  @Test
  void shouldReturn404WhenUserNotFound() throws Exception {
    String token = createUserAndGetToken("auth-404@example.com");

    mockMvc.perform(get("/users/" + UUID.randomUUID())
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn401WithoutToken() throws Exception {
    mockMvc.perform(get("/users"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldListUsers() throws Exception {
    String token = createUserAndGetToken("list@example.com");

    mockMvc.perform(get("/users")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());

    mockMvc.perform(get("/users")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void shouldReturn400WithInvalidPagination() throws Exception {
    String token = createUserAndGetToken("pagination@example.com");

    mockMvc.perform(get("/users?page=0&size=0")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest());
  }

  private void createUser(String email) throws Exception {
    mockMvc.perform(multipart("/users")
        .file(new MockMultipartFile("image", "img.jpg", "image/jpeg", "fake".getBytes()))
        .param("name", "Test User")
        .param("phone", "11987654321")
        .param("email", email)
        .param("password", "password123"));
  }

  private String createUserAndGetToken(String email) throws Exception {
    createUser(email);
    return getToken(email, "password123");
  }

  private String getToken(String email, String password) throws Exception {
    String response = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    return objectMapper.readTree(response).get("token").asText();
  }
}
