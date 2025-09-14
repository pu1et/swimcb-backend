package com.project.swimcb.oauth2.application;

import static com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType.*;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.application.port.out.OAuth2MemberGateway;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.KakaoOAuth2Request;
import com.project.swimcb.oauth2.domain.OAuth2Member;
import com.project.swimcb.oauth2.domain.SignupRequest;
import com.project.swimcb.oauth2.domain.enums.Environment;
import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import com.project.swimcb.token.application.in.GenerateCustomerTokenUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class KakaoOAuth2Interactor implements OAuth2Adapter<KakaoOAuth2Request> {

  private final OAuth2MemberGateway oAuth2MemberGateway;
  private final FindMemberPort findMemberPort;
  private final SignupPort signupPort;
  private final OAuth2Presenter oAuth2Presenter;
  private final GenerateCustomerTokenUseCase generateCustomerTokenUseCase;

  @Override
  public OAuth2Response success(@NonNull KakaoOAuth2Request request) {
    val oAuth2Member = oAuth2MemberGateway.resolve(request.code());
    val member = findMemberPort.findByEmail(oAuth2Member.email());

    if (member.isEmpty()) {
      return signup(oAuth2Member, request.env());
    }
    return login(member.get().id(), request.env());
  }

  private OAuth2Response signup(@NonNull OAuth2Member oAuth2Member, Environment env) {
    val memberId = signupPort.signup(
        SignupRequest.builder()
            .email(oAuth2Member.email())
            .name(oAuth2Member.name())
            .phoneNumber(oAuth2Member.phoneNumber())
            .providerType(KAKAO)
            .build()
    );
    val token = generateCustomerTokenUseCase.generateCustomerToken(memberId);
    return oAuth2Presenter.signup(token, env);
  }

  private OAuth2Response login(@NonNull Long memberId, Environment env) {
    val token = generateCustomerTokenUseCase.generateCustomerToken(memberId);
    return oAuth2Presenter.login(token, env);
  }

}
