package com.my.bob.v1.member.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.service.member.BobUserService;
import com.my.bob.v1.member.repository.BobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BobUserServiceImpl implements BobUserService {

    private final BobRepository bobRepository;

    public boolean existByEmail(String email) {
        return bobRepository.existsByEmail(email);
    }

    public BobUser getById(long userId) {
        return bobRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_CANNOT_BE_FOUND));
    }

    public BobUser getByEmail(String email) {
        return bobRepository.findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_CANNOT_BE_FOUND));
    }

    @Transactional
    public void save(BobUser bobUser) {
        bobRepository.save(bobUser);
    }

}