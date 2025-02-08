package com.project.swimcb.config.s3;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class S3Config {

  @Value("${aws.credentials.access-key}")
  private String accessKey;

  @Value("${aws.credentials.secret-key}")
  private String secretKey;

  @Value("${aws.s3.region}")
  private String region;

  @Getter
  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  @Getter
  private S3Client s3Client;

  @PostConstruct
  public void init() {
    val credentials = AwsBasicCredentials.create(accessKey, secretKey);

    this.s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(credentials))
        .build();
  }
}
