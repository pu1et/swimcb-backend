package com.project.swimcb.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ContentTypeValidator implements ConstraintValidator<ContentType, MultipartFile> {

  private List<String> allowedTypes;

  @Override
  public void initialize(ContentType contentType) {
    allowedTypes = Arrays.asList(contentType.allowedTypes());
  }

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (file == null || file.isEmpty()) {
      return false;
    }

    return allowedTypes.contains(file.getContentType());
  }
}
