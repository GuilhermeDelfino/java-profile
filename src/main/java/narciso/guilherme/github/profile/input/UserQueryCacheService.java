package narciso.guilherme.github.profile.input;

import narciso.guilherme.github.profile.core.service.UserService;
import narciso.guilherme.github.profile.input.dto.UserResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserQueryCacheService {

  public static final String USER_BY_ID_CACHE = "user-response-v1";
  public static final String USER_LIST_CACHE = "user-response-list-v1";

  private final UserService userService;

  public UserQueryCacheService(UserService userService) {
    this.userService = userService;
  }

  @Cacheable(value = USER_BY_ID_CACHE, key = "#id", unless = "#result == null || #result.isEmpty()")
  public Optional<UserResponse> findById(UUID id) {
    return userService.findById(id).map(UserResponse::from);
  }

  @Cacheable(value = USER_LIST_CACHE, key = "#page + '-' + #size")
  public List<UserResponse> findAll(int page, int size) {
    return userService.findAllPageable(page, size).stream().map(UserResponse::from).toList();
  }

  @Caching(evict = {
      @CacheEvict(value = USER_BY_ID_CACHE, allEntries = true),
      @CacheEvict(value = USER_LIST_CACHE, allEntries = true)
  })
  public void evictAll() {
    // Triggered after user creation to invalidate cached projections.
  }
}
