package com.project.swimcb.oauth2.adapter.out;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.project.swimcb.oauth2.adapter.out.exception.OAuth2TokenRequestException;
import com.project.swimcb.oauth2.application.port.in.OAuth2ClientInfoProvider;
import com.project.swimcb.oauth2.application.port.out.OAuth2MemberGateway;
import com.project.swimcb.oauth2.domain.OAuth2AccessTokenResponse;
import com.project.swimcb.oauth2.domain.OAuth2Member;
import com.project.swimcb.oauth2.domain.OAuth2MemberInfoResponse;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
class OAuth2MemberResolver implements OAuth2MemberGateway {

  private final OAuth2ClientInfoProvider oAuth2ClientInfoProvider;

  @Override
  public OAuth2Member resolve(@NonNull String code) {
    val accessToken = getAccessToken(code);
    return getOAuth2MemberUsingToken(accessToken);
  }

  private OAuth2Member getOAuth2MemberUsingToken(@NonNull String accessToken) {
    val clientInfo = oAuth2ClientInfoProvider.getOAuth2ClientInfo();

    val response = WebClient.builder()
        .baseUrl(clientInfo.provider().memberInfoUri())
        .defaultHeader("Authorization", "Bearer " + accessToken)
        .build()
        .get()
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, res ->
            res.bodyToMono(String.class)
                .map(body -> new OAuth2TokenRequestException(
                    "OAuth2 Client member info error: " + body)
                )
        )
        .onStatus(HttpStatusCode::is5xxServerError, res ->
            res.bodyToMono(String.class)
                .map(body -> new OAuth2TokenRequestException(
                    "OAuth2 Server member info error: " + body)
                )
        )
        .bodyToMono(OAuth2MemberInfoResponse.class)
        .block();

    log.info("OAuth2 Member Info : {}", response);

    if (response == null || response.kakaoAccount() == null) {
      throw new OAuth2TokenRequestException("OAuth2 member info response is null or invalid");
    }

    if (response.kakaoAccount().phoneNumber() == null) {
      throw new OAuth2TokenRequestException("카카오 회원의 전화번호가 존재하지 않습니다. 카카오톡을 연결해주세요.");
    }

    return OAuth2Member.builder()
        .name(response.kakaoAccount().name())
        .email(response.kakaoAccount().email())
        .phoneNumber(response.kakaoAccount().phoneNumber())
        .build();
  }

  private String getAccessToken(@NonNull String code) {
    val clientInfo = oAuth2ClientInfoProvider.getOAuth2ClientInfo();

    val formData = MultiValueMap.fromSingleValue(Map.of(
        "grant_type", "authorization_code",
        "client_id", clientInfo.registration().clientId(),
        "client_secret", clientInfo.registration().clientSecret(),
        "redirect_uri", clientInfo.registration().redirectUri(),
        "code", code
    ));

    val response = WebClient.builder()
        .baseUrl(clientInfo.provider().tokenUri())
        .defaultHeader("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
        .build()
        .post()
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, res ->
            res.bodyToMono(String.class)
                .map(body -> new OAuth2TokenRequestException("OAuth2 Client token error: " + body)
                )
        )
        .onStatus(HttpStatusCode::is5xxServerError, res ->
            res.bodyToMono(String.class)
                .map(body -> new OAuth2TokenRequestException("OAuth2 Server token error: " + body)
                )
        )
        .bodyToMono(OAuth2AccessTokenResponse.class)
        .block();

    if (response == null) {
      throw new OAuth2TokenRequestException("OAuth2 token response is null");
    }

    return response.accessToken();
  }

}
