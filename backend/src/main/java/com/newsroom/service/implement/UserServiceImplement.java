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
    public User handleGetUserByUserName(String identifier) {
        if (identifier == null || identifier.isBlank()) return null;
        String id = identifier.trim();
        User user = this.userRepository.findByEmail(id.toLowerCase());
        if (user != null) return user;
        user = this.userRepository.findByPhone(id);
        if (user != null) return user;
        return this.userRepository.findByUsername(id).orElse(null);
    }

    @Override
    public void updateUserToken(String refreshToken, String identifier) {
        if (identifier == null || identifier.isBlank()) return;
        User currentUser = this.handleGetUserByUserName(identifier);
        if (currentUser != null) {
            currentUser.setRefreshToken(refreshToken);
            this.userRepository.save(currentUser);
        }
    }
}
