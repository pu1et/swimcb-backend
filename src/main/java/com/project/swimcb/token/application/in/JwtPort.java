package com.project.swimcb.token.application.in;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.swimcb.token.domain.TokenInfo;
import lombok.NonNull;

public interface JwtPort {

  String generateToken(@NonNull TokenInfo tokenInfo);

  DecodedJWT parseToken(@NonNull String token);
}
