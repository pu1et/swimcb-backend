package com.project.swimcb.oauth2.application;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.AppleOAuth2Request;
import com.project.swimcb.oauth2.domain.SignupRequest;
import com.project.swimcb.token.application.in.GenerateCustomerTokenUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class AppleOAuth2Interactor implements OAuth2Adapter<AppleOAuth2Request> {

  private final FindMemberPort findMemberPort;
  private final SignupPort signupPort;
  private final OAuth2Presenter oAuth2Presenter;
  private final GenerateCustomerTokenUseCase generateCustomerTokenUseCase;

  @Override
  public OAuth2Response success(@NonNull AppleOAuth2Request request) {
    val member = findMemberPort.findByEmail(request.email());

    if (member.isEmpty()) {
      return signup(request.email());
    }
    return login(member.get().id());
  }

  private OAuth2Response signup(@NonNull String email) {
    val memberId = signupPort.signup(
        SignupRequest.builder()
            .email(email)
            .build()
    );
    val token = generateCustomerTokenUseCase.generateCustomerToken(memberId);
    return oAuth2Presenter.signup(token, null);
  }

  private OAuth2Response login(@NonNull Long memberId) {
    val token = generateCustomerTokenUseCase.generateCustomerToken(memberId);
    return oAuth2Presenter.login(token, null);
  }

}
