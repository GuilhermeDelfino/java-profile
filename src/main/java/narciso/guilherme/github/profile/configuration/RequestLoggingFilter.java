package narciso.guilherme.github.profile.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(-101)
public class RequestLoggingFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    long start = System.currentTimeMillis();
    String method = request.getMethod();
    String path = request.getRequestURI();

    log.info("Incoming request: {} {}", method, path);

    try {
      chain.doFilter(request, response);
    } finally {
      long duration = System.currentTimeMillis() - start;
      int status = response.getStatus();

      if (status >= 500) {
        log.error("Completed request: {} {} -> {} ({}ms)", method, path, status, duration);
      } else if (status >= 400) {
        log.warn("Completed request: {} {} -> {} ({}ms)", method, path, status, duration);
      } else {
        log.info("Completed request: {} {} -> {} ({}ms)", method, path, status, duration);
      }
    }
  }
}
