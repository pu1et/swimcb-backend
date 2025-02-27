package com.project.swimcb.common;

import static com.project.swimcb.token.domain.enums.MemberRole.CUSTOMER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.project.swimcb.token.domain.enums.MemberRole;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RUNTIME)
@Target({METHOD, TYPE})
@WithSecurityContext(factory = WithMockTokenInfoSecurityContextFactory.class)
public @interface WithMockTokenInfo {

  String memberId() default "";

  MemberRole role() default CUSTOMER;

  String swimmingPoolId() default "";
}
