package com.project.swimcb.bo.swimmingpool.adapter.out;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.config.s3.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageUrl implements ImageUrlPort {

  private final S3Config s3Config;

  @Override
  public String getImageUrl(String imagePath) {
    if (imagePath == null) {
      return null;
    }
    return s3Config.getHost() + "/" + s3Config.getBucketName() + "/" + imagePath;
  }
}
