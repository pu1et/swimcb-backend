package com.project.swimcb.config.security;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/**").permitAll()
            .anyRequest().authenticated())
        .headers(i -> i.frameOptions(FrameOptionsConfig::disable))
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    val corsConfiguration = new CorsConfiguration();
    // 사용자앱
    corsConfiguration.addAllowedOrigin("http://localhost:4000");
    corsConfiguration.addAllowedOrigin("https://cbe-user.pages.dev");

    // 판매자앱
    corsConfiguration.addAllowedOrigin("http://localhost:4001");
    corsConfiguration.addAllowedOrigin("https://cbe-center.pages.dev");

    corsConfiguration.addAllowedOrigin("http://localhost:4010");

    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
