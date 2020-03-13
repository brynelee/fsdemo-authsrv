package com.xdorg1.authsrv.service;

import com.xdorg1.authsrv.model.UserEntity;

public interface UserService {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    UserEntity findByUsername(String username);
}
