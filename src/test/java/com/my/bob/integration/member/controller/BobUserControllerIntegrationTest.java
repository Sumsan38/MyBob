package com.my.bob.integration.member.controller;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.constants.FailCode;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.dto.LoginDto;
import com.my.bob.core.domain.member.dto.TokenDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.member.service.JoinService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 회원가입, 로그인")
class BobUserControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JoinService joinService;

    @Autowired
    private BobUserRepository bobUserRepository;

    private final String baseUrl = "/api/v1/member/";

    private final String email = "test__user@test.com";
    private final String password = "correctPassword1234!";

    private final String successMessage = "SUCCESS";
    private final String failMessage = "FAIL";

    @AfterEach
    public void cleanUp() {
        bobUserRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 실패 - valid 체크 실패(잘못된 비밀번호)")
    void joinMember_fail_invalidPassword() {
        JoinUserDto dto = new JoinUserDto();
        dto.setEmail(email);
        dto.setPassword("WRONG_PASSWORD");

        webTestClient.post()
                .uri(baseUrl + "join")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertThat(responseDto.getStatus()).isEqualTo(failMessage);
                    assertThat(responseDto.getErrorCode()).isEqualTo(FailCode.V_00001.name());
                    assertThat(responseDto.getErrorMessage()).isEqualTo(ErrorMessage.INVALID_PASSWORD);
                });
    }


    @Test
    @DisplayName("회원 가입 실패 - valid 체크 실패(잘못된 이메일 형식)")
    void joinMember_fail_invalidEmail() {
        JoinUserDto dto = new JoinUserDto();
        dto.setEmail("WRONG_EMAIL");
        dto.setPassword(password);

        webTestClient.post()
                .uri(baseUrl + "join")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertThat(responseDto.getStatus()).isEqualTo(failMessage);
                    assertThat(responseDto.getErrorCode()).isEqualTo(FailCode.V_00001.name());
                    assertThat(responseDto.getErrorMessage()).isEqualTo(ErrorMessage.INVALID_EMAIL);
                });
    }

    @Test
    @DisplayName("회원 가입 성공")
    void joinMember_success() {
        // given
        JoinUserDto dto = new JoinUserDto();
        dto.setEmail(email);
        dto.setPassword(password);

        webTestClient.post()
                .uri(baseUrl + "join")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(responseDto -> assertThat(responseDto.getStatus()).isEqualTo(successMessage));

    }

    @Test
    @DisplayName("로그인 실패 - 존재 하지 않는 유저")
    void login_fail_invalidEmail() {
        // given
        joinTestUser();

        LoginDto dto = new LoginDto();
        dto.setEmail("WRONG_USER@test.com");
        dto.setPassword(password);

        // when & then
        webTestClient.post()
                .uri(baseUrl + "/login")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertThat(responseDto.getStatus()).isEqualTo(failMessage);
                    assertThat(responseDto.getErrorCode()).isEqualTo(FailCode.V_00001.name());
                    assertThat(responseDto.getErrorMessage()).isEqualTo(ErrorMessage.NEED_TO_CONFIRM_LOGIN_INFORMATION);
                });
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_invalidPassword() {
        // given
        joinTestUser();

        LoginDto dto = new LoginDto();
        dto.setEmail(email);
        dto.setPassword("WRONG_PASSWORD");

        // when & then
        webTestClient.post()
                .uri(baseUrl + "/login")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertThat(responseDto.getStatus()).isEqualTo(failMessage);
                    assertThat(responseDto.getErrorCode()).isEqualTo(FailCode.V_00001.name());
                    assertThat(responseDto.getErrorMessage()).isEqualTo(ErrorMessage.NEED_TO_CONFIRM_PASSWORD);
                });
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        joinTestUser();

        LoginDto dto = new LoginDto();
        dto.setEmail(email);
        dto.setPassword(password);

        // when& then
        webTestClient.post()
                .uri(baseUrl + "/login")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<TokenDto>>() {
                })
                .value(tokenDtoResponseDto -> {
                    assertThat(tokenDtoResponseDto.getStatus()).isEqualTo(successMessage);
                    assertThat(tokenDtoResponseDto.getData()).isNotNull();

                    TokenDto data = tokenDtoResponseDto.getData();
                    assertThat(data.getAccessToken()).isNotNull();
                    assertThat(data.getAccessTokenExpire()).isAfter(LocalDateTime.now());
                    assertThat(data.getRefreshToken()).isNotNull();
                    assertThat(data.getRefreshTokenExpire()).isAfter(LocalDateTime.now());
                });
    }


    private void joinTestUser() {
        JoinUserDto dto = new JoinUserDto();
        dto.setEmail(email);
        dto.setPassword(password);

        try {
            joinService.joinMember(dto);
        } catch (DuplicateUserException e) {
            fail("Fail to join member.");
        }
    }
}
