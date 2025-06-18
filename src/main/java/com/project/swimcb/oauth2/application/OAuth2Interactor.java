package com.project.swimcb.oauth2.application;

import com.project.swimcb.oauth2.adapter.in.OAuth2Request;
import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.application.port.out.OAuth2MemberGateway;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.OAuth2Member;
import com.project.swimcb.oauth2.domain.SignupRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class OAuth2Interactor implements OAuth2Adapter {

  private final OAuth2MemberGateway oAuth2MemberGateway;
  private final FindMemberPort findMemberPort;
  private final SignupPort signupPort;
  private final OAuth2Presenter oAuth2Presenter;

  @Override
  public OAuth2Response success(@NonNull OAuth2Request request) {
    val oAuth2Member = oAuth2MemberGateway.resolve(request.code());
    val member = findMemberPort.findByEmail(oAuth2Member.email());

    if (member.isEmpty()) {
      return signup(oAuth2Member);
    }
    return oAuth2Presenter.login();
  }

  private OAuth2Response signup(@NonNull OAuth2Member oAuth2Member) {
    signupPort.signup(
        SignupRequest.builder()
            .email(oAuth2Member.email())
            .name(oAuth2Member.name())
            .phoneNumber(oAuth2Member.phoneNumber())
            .build()
    );
    return oAuth2Presenter.signup();
  }

}
