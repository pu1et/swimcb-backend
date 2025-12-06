package com.project.swimcb.config.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.domain.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

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
      filterChain.doFilter(request, response);
      return;
    }

    val token = header.substring(TOKEN_PREFIX.length());
    try {
      val decodedJWT = jwtPort.parseToken(token);
      val tokenInfo = TokenInfo.fromToken(decodedJWT);
      val authority = List.of(new SimpleGrantedAuthority(tokenInfo.role().name()));
      val authentication = new UsernamePasswordAuthenticationToken(tokenInfo, null, authority);

      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
    } catch (SignatureVerificationException e) {
      request.setAttribute("errorMessage", "잘못된 JWT 서명입니다.");
      filterChain.doFilter(request, response);
    } catch (TokenExpiredException e) {
      request.setAttribute("errorMessage", "만료된 JWT 서명입니다.");
      filterChain.doFilter(request, response);
    }
  }

}
