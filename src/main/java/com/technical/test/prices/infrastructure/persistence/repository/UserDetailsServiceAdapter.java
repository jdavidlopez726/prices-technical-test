package com.technical.test.prices.infrastructure.persistence.repository;

import com.technical.test.prices.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements Spring Security's {@link UserDetailsService} port with the {@code users} table.
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceAdapter implements UserDetailsService {

    private final JpaUserRepository jpaUserRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return jpaUserRepository.findByUsername(username)
                .map(UserDetailsServiceAdapter::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private static UserDetails toUserDetails(UserEntity user) {
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .roles(user.getRole().getRoleName())
                .build();
    }
}
