package com.example.umc.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Authorization: Bearer {accessToken} 헤더로 JWT 전달하는 방식이므로 굳이 필요하지 않음 -> 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // 밑에 `@Bean`으로 등록해둔 corsConfigurationSource을 보고 CORS를 처리
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 현재는 SecurityFilter가 JWT 토큰을 검증하는 방식이 아님. (워크북과 다름!)
                // 현재는 Service 코드에서 검증하는 방식을 사용한다. 그리고, 오직 /rooms/vote만 JWT 토큰을 필요로 한다.
                // 이렇게 특정 API만 JWT 토큰이 필요할 때는 일단 모든 API에게 접근을 허용하고 해당 API만 토큰을 검증하도록 한다.
                // 그래서 아래와 같이 처리함.
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().permitAll()
                )
                ;
        return http.build();
    }

    // CORS 설정 (SecurityFilter에서 이 설정으로 복 CORS를 처리한다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        )); // 허용하는 Origins
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 해당 Origins에서 처리가능한 Method
        config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // 허용된 Origin의 브라우저가 CORS 요청 시 보낼 수 있도록 허용할 요청 헤더
        config.setAllowCredentials(false); // Cross-Origin 요청에 쿠키 등의 인증 정보를 포함하지 않도록 설정. Credentials : 쿠키, 세션 쿠키, 브라우저 HTTP 인증 정보, 클라이언트 인증서 등
        config.setMaxAge(3600L); // Preflight 요청의 검사 결과를 브라우저가 3,600초, 즉 1시간 동안 캐시해도 된다는 뜻.
        // Preflight이란 브라우저가 실제 요청을 보내기 전에 서버에 먼저 묻는 사전 확인 요청이다. Origin이 다른 곳에서 리소스를 필요로 할 때, 먼저 서버에게 써도 되는 지 물어보는 OPTIONS` 요청이다.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 경로별로 CORS 설정을 저장하고 찾아주는 객체 만든다.
        source.registerCorsConfiguration("/**", config); // 앞에서 만든 config를 백엔드의 모든 요청 경로에 적용한다.
        return source;
    }
}
