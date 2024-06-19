package com.my.bob.config;

import com.my.bob.filter.JwtAuthenticationFilter;
import com.my.bob.user.service.CustomerUserDetailService;
import com.my.bob.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.my.bob")
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerUserDetailService customerUserDetailService;

    private final static String[] PERMIT_ALL = {
            "/test/**",
            "/user/join",
            "/user/login",
            "/user/reissue"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                  // csrf 보안 disable
                .formLogin(AbstractHttpConfigurer::disable)             // formLogin disable
                .httpBasic(AbstractHttpConfigurer::disable)             // BasicHttp disable

                // 토큰을 통한 로그인. 사용 X session stateless
                .sessionManagement((sessionManager) ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 로그아웃 관련


                // 권한 없이 접근할 api
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(PERMIT_ALL).permitAll().anyRequest().authenticated())


                // 로그인 관련
                // UsernamePasswordAuthenticationFilter 전에 jwt Token 관련 Filter 진행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customerUserDetailService), UsernamePasswordAuthenticationFilter.class)

        ;


        return http.build();
    }


    // 암호화와 관련된 모든 설정 값 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
