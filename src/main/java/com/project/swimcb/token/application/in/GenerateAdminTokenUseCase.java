package com.project.swimcb.token.application.in;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public interface GenerateAdminTokenUseCase {

  String generateAdminToken(@NonNull String loginId, @NotNull String password);
}
