package narciso.guilherme.github.profile.input;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import narciso.guilherme.github.profile.core.service.JwtService;
import narciso.guilherme.github.profile.input.dto.AuthResponse;
import narciso.guilherme.github.profile.input.dto.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication")
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  @Operation(summary = "Authenticate and get JWT token")
  @ApiResponse(responseCode = "200", description = "Authenticated successfully")
  @ApiResponse(responseCode = "401", description = "Invalid credentials")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    log.info("Login attempt for email={}", request.email());
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    String token = jwtService.generateToken(authentication.getName());
    log.info("Login successful for email={}", request.email());
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
