package com.newsroom.service;

import com.newsroom.model.User;

public interface IUserService {
    User handleGetUserByUserName(String username);

    void updateUserToken(String refreshToken, String username);
}
