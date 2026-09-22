package com.newsroom.repository;

import com.newsroom.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    User findFirstByPhone(String phone);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);

    User findByRefreshTokenAndEmail(String token, String email);
}

