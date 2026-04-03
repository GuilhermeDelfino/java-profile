package narciso.guilherme.github.profile.output.aws;

import narciso.guilherme.github.profile.core.output.SaveImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Component
public class S3SaveImage implements SaveImage {

  private final S3Client s3Client;
  private final String bucket;
  private final String region;

  public S3SaveImage(
      S3Client s3Client,
      @Value("${aws.s3.bucket}") String bucket,
      @Value("${aws.s3.region}") String region) {
    this.s3Client = s3Client;
    this.bucket = bucket;
    this.region = region;
  }

  @Override
  public String save(InputStream image, long contentLength, String contentType) {
    String key = "profile-pictures/" + UUID.randomUUID();

    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(contentType)
            .build(),
        RequestBody.fromInputStream(image, contentLength));

    return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucket, region, key);
  }
}
