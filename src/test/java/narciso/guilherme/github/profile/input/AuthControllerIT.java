package narciso.guilherme.github.profile.input;

import narciso.guilherme.github.profile.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIT extends AbstractIntegrationTest {

  @BeforeEach
  void setupAuthUser() throws Exception {
    when(saveImage.save(any(), anyLong(), anyString())).thenReturn("https://s3.example.com/img.jpg");

    mockMvc.perform(multipart("/users")
        .file(new MockMultipartFile("image", "img.jpg", "image/jpeg", "fake".getBytes()))
        .param("name", "Auth User")
        .param("phone", "11987654321")
        .param("email", "auth@example.com")
        .param("password", "password123"));
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"auth@example.com\",\"password\":\"password123\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @Test
  void shouldReturn401WithWrongPassword() throws Exception {
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"auth@example.com\",\"password\":\"wrong\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturn401WithNonexistentEmail() throws Exception {
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"ghost@example.com\",\"password\":\"password123\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturn400WithInvalidPayload() throws Exception {
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"not-an-email\",\"password\":\"\"}"))
        .andExpect(status().isBadRequest());
  }
}
