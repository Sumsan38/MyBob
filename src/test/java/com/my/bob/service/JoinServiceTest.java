package com.my.bob.service;

import com.my.bob.member.dto.JoinUserDto;
import com.my.bob.exception.DuplicateUserException;
import com.my.bob.member.service.BobUserService;
import com.my.bob.member.service.JoinService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class JoinServiceTest {

    @Autowired
    private BobUserService bobUserService;


    @Autowired
    private JoinService joinService;


    @Test
    @Transactional
    @DisplayName("회원 가입")
    public void joinUser(){
//        Given - 테스트를 위한 준비(테스트를 할 수 있는 상황, 객체나 데이터 또는 Mock을 셋팅)
        String testEmail = "sss@naver.com";
        JoinUserDto joinUserDto = new JoinUserDto();
        joinUserDto.setEmail(testEmail);
        joinUserDto.setPassword("test1234!");


//        When - 테스트하는 메서드를 실행(어떤 메서드를 실행했을때)
        try {
            joinService.joinMember(joinUserDto);
        } catch (DuplicateUserException e) {
            log.error("another email needs.");
            fail();
        }

//        Then - 테스트 결과 검증(그 메서드를 실행함으로서 기대 되는 결과)
        assertTrue(bobUserService.existByEmail(testEmail));
    }

    @Test
    @Transactional
    @DisplayName("회원 가입 실패 - 중복 이메일 확인")
    public void joinUserFail(){
        // TODO
    }

}