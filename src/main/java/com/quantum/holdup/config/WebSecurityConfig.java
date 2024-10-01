package com.quantum.holdup.config;

import com.quantum.holdup.filter.CustomAuthenticationFilter;
import com.quantum.holdup.filter.JwtAuthorizationFilter;
import com.quantum.holdup.handler.CustomAuthFailUserHandler;
import com.quantum.holdup.handler.CustomAuthSuccessHandler;
import com.quantum.holdup.handler.CustomAuthenticationProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * 1. 정적 자원에 대한 인증된 사용자의 접근을 설정하는 메서드
     *
     * @return WebSeruciryCusomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        // 특정 요청에대해 spring security filter chain을 건너뛰도록 하는 역할

        // WebConfig에 설정한 addResourceHandler는 정적 자원에 대해 요청을 할 수 있게 해주는 역할
        // webSecurityCustomizer 는 특정 요청에 대해 filterChain을 건너뛰도록 설정하는 역할
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF 보호 비활성화 (REST API 혹은 JWT 사용 시)
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 상태를 비저장 모드로 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 기본 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP 기본 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 사용자 인증 필터 추가
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // JWT 인증 필터 추가
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/holdup/signup", // 회원가입
                                "/holdup/login", // 로그인
                                "/holdup/find-email", // 이메일 찾기
                                "/holdup/check-nickname", // 닉네임 중복검사
                                "/holdup/check-email", // 이메일 중복검사
                                "/holdup/signup-send-verification-code", // 이메일 인증코드 전송
                                "/holdup/signup-verify-code", // 이메일 인증코드 확인
                                "/holdup/send-verification-code", // 비밀번호 이메일 인증코드 전송
                                "/holdup/verify-code", // 비밀번호 이메일 인증코드 확인
                                "/v3/api-docs/**",
                                "/swagger-ui/**", // 스웨거 설정
                                "/swagger-resources/**" // 스웨거 설정
                        ).permitAll()
                        .requestMatchers("/holdup/update").authenticated() // /update 경로 인증 필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                );

        return http.build();
    }


    /**
     * 3. Authentization의 인증 메서드를 제공하는 매니저로 Provider의 인터페이스를 의미한다.
     *
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    /**
     * 4. 사용자의 아이디와 패스워드를 DB와 검증하는 handler이다.
     *
     * @return CustomAuthenticationProvider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    /**
     * 비밀번호를 암호화 하는 인코더
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 6. 사용자의 인증 요청을 가로채서 로그인 로직을 수행하는 필터
     *
     * @return CustomAuthenticationFilter
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());

        // /login 으로 post 요청이 들어오면 필터가 동작한다.
        customAuthenticationFilter.setFilterProcessesUrl("/holdup/login");

        // 인증 성공시 동작할 핸들러 설정
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthLoginSuccessHandler());

        // 인증 실패시 동작할 핸들러 설정
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailUserHandler());

        // 필터의 모든 속성 설정을 완료했을때
        // 올바르게 설정되어있는지 확인하는 역할의 메서드
        customAuthenticationFilter.afterPropertiesSet();

        // 완성된 CustomAuthenticationFilter 를 반환한다.
        return customAuthenticationFilter;
    }

    /**
     * 7. spring security 기반의 사용자의 정보가 맞을 경우 결과를 수행하는 handler
     *
     * @return customAuthLoginSuccessHandler
     */
    @Bean
    public CustomAuthSuccessHandler customAuthLoginSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    /**
     * 8. Spring security의 사용자 정보가 맞지 않은 경우 행되는 메서드
     *
     * @return CustomAuthFailUreHandler
     */
    @Bean
    public CustomAuthFailUserHandler customAuthFailUserHandler() {
        return new CustomAuthFailUserHandler();
    }

    /**
     * 9. 사용자 요청시 수행되는 메소드
     *
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(authenticationManager());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
