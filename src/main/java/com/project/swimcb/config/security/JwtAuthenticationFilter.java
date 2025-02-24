package com.project.swimcb.config.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.domain.TokenInfo;
import com.project.swimcb.token.domain.enums.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtPort jwtPort;

  private static final String TOKEN_PREFIX = "Bearer ";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    val header = request.getHeader("Authorization");
    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      throw new MissingAuthorizationHeaderException("Authorization header is null or invalid.");
    }

    val token = header.substring(TOKEN_PREFIX.length());
    try {
      val decodedJWT = jwtPort.parseToken(token);
      val memberId = Long.parseLong(decodedJWT.getSubject());
      val isGuest = decodedJWT.getClaim("isGuest").asBoolean();
      val role = MemberRole.valueOf(decodedJWT.getClaim("role").asString());

      val tokenInfo = new TokenInfo(memberId, isGuest, role);
      val authority = List.of(new SimpleGrantedAuthority(role.name()));
      val authentication = new UsernamePasswordAuthenticationToken(tokenInfo, null, authority);

      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
    } catch (SignatureVerificationException e) {
      log.error("잘못된 JWT 서명입니다.", e);
    } catch (TokenExpiredException e) {
      log.error("만료된 JWT 서명입니다.", e);
    }
  }
}
