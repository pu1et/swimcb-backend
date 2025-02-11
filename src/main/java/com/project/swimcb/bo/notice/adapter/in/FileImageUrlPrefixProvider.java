package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.config.s3.S3Config;
import com.project.swimcb.bo.notice.application.out.ImageUrlPrefixProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileImageUrlPrefixProvider implements ImageUrlPrefixProvider {

  private final S3Config s3Config;

  @Override
  public String provide() {
    return "https://s3.amazonaws.com/" + s3Config.getBucketName() + "/";
  }
}
