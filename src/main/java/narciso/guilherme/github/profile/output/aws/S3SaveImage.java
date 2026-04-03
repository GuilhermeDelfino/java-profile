package narciso.guilherme.github.profile.output.aws;

import narciso.guilherme.github.profile.core.output.SaveImage;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class S3SaveImage implements SaveImage {
  @Override
  public String save(InputStream image) {
    return "";
  }
}
