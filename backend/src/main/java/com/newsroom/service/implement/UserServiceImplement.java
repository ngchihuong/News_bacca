package com.newsroom.service.implement;

import com.newsroom.model.User;
import com.newsroom.repository.UserRepository;
import com.newsroom.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements IUserService {
    private final UserRepository userRepository;

    @Override
    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }

    @Override
    public void updateUserToken(String refreshToken, String username) {
        User currentUser = this.userRepository.findByEmail(username);
        if (currentUser != null) {
            currentUser.setRefreshToken(refreshToken);
            this.userRepository.save(currentUser);
        }
    }
}
