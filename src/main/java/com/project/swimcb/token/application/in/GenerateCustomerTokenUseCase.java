package com.project.swimcb.token.application.in;

import lombok.NonNull;

public interface GenerateCustomerTokenUseCase {

  String generateCustomerToken(@NonNull Long memberId);
}
