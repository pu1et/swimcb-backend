package com.project.swimcb.config.security;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/token/**"
            ).permitAll()
            .anyRequest().authenticated())
        .headers(i -> i.frameOptions(FrameOptionsConfig::disable))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    val corsConfiguration = new CorsConfiguration();
    // 사용자앱
    corsConfiguration.addAllowedOrigin("http://localhost:4000");
    corsConfiguration.addAllowedOrigin("https://cb-user.vercel.app");

    // 판매자앱
    corsConfiguration.addAllowedOrigin("http://localhost:4001");
    corsConfiguration.addAllowedOrigin("https://cb-center.vercel.app");

    corsConfiguration.addAllowedOrigin("http://localhost:4010");

    // FE 개발자 로컬 폰 (사용자 앱 테스트할 때 사용)
    corsConfiguration.addAllowedOrigin("http://192.168.0.37:4000");

    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
