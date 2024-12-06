package com.my.bob.core.service.member;

import com.my.bob.core.domain.member.entity.BobUser;

public interface BobUserService {

    boolean existByEmail(String email);

    BobUser getById(long userId);

    BobUser getByEmail(String email);

    void save(BobUser bobUser);
}