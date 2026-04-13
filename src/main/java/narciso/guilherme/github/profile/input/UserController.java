package narciso.guilherme.github.profile.input;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import narciso.guilherme.github.profile.core.service.UserService;
import narciso.guilherme.github.profile.input.dto.CreateUserDTO;
import narciso.guilherme.github.profile.input.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management")
public class UserController {

  private final UserService userService;
  private final UserQueryCacheService userQueryCacheService;
  private final PasswordEncoder passwordEncoder;

  public UserController(
      UserService userService,
      UserQueryCacheService userQueryCacheService,
      PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.userQueryCacheService = userQueryCacheService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Create a new user")
  @ApiResponse(responseCode = "201", description = "User created successfully")
  public ResponseEntity<UserResponse> create(@Valid @ModelAttribute CreateUserDTO dto)
    throws IOException {
    var user =
      userService.save(
        dto.getName(), dto.getPhone(), dto.getEmail(),
        passwordEncoder.encode(dto.getPassword()),
        dto.getImage().getInputStream(), dto.getImage().getSize(),
        dto.getImage().getContentType());
    userQueryCacheService.evictAll();
    var response = UserResponse.from(user);
    return ResponseEntity.created(URI.create("/users/" + response.id())).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find user by ID")
  @ApiResponse(responseCode = "200", description = "User found")
  @ApiResponse(responseCode = "404", description = "User not found")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
    return userQueryCacheService
      .findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  @Operation(summary = "List users with pagination")
  @ApiResponse(responseCode = "200", description = "Users listed successfully")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Collection<UserResponse>> findAll(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
    List<UserResponse> list = userQueryCacheService.findAll(page, size);
    return ResponseEntity.ok(list);
  }
}
