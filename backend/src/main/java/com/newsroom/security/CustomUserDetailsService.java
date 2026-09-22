package com.newsroom.security;

import com.newsroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public com.newsroom.model.User findUserByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) return null;
        String id = identifier.trim();
        com.newsroom.model.User user = this.userRepository.findByEmail(id.toLowerCase());
        if (user != null) return user;
        user = this.userRepository.findFirstByPhone(id);
        if (user != null) return user;
        return this.userRepository.findByUsername(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.newsroom.model.User user = findUserByIdentifier(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        boolean accountNonLocked = user.getLockoutUntil() == null || user.getLockoutUntil().isBefore(java.time.Instant.now());

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            authorities.add(new SimpleGrantedAuthority(user.getRole().toUpperCase()));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return User.builder()
                .username(user.getEmail() != null ? user.getEmail() : user.getUsername())
                .password(user.getPassword())
                .accountLocked(!accountNonLocked)
                .disabled(!user.isActive())
                .authorities(authorities)
                .build();
    }
}

