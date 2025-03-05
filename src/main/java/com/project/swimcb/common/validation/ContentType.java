package com.project.swimcb.common.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ContentTypeValidator.class)
public @interface ContentType {

  String message() default "지원하지 않는 파일 형식입니다.";

  String[] allowedTypes() default {};

  Class[] groups() default {};

  Class[] payload() default {};
}

