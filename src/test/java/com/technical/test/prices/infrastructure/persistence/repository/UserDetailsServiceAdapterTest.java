package com.technical.test.prices.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.technical.test.prices.infrastructure.persistence.entity.RoleEntity;
import com.technical.test.prices.infrastructure.persistence.entity.UserEntity;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceAdapterTest {

    private static final String USERNAME = "user";
    private static final String ENCODED_PASSWORD = "{bcrypt}encoded-password";

    @Mock
    private JpaUserRepository jpaUserRepository;

    @InjectMocks
    private UserDetailsServiceAdapter userDetailsServiceAdapter;

    @Test
    void givenExistingUser_whenLoadUserByUsername_thenReturnsUserWithItsRole() {
        when(jpaUserRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user(true, "USER")));

        UserDetails result = userDetailsServiceAdapter.loadUserByUsername(USERNAME);

        assertThat(result.getUsername()).isEqualTo(USERNAME);
        assertThat(result.getPassword()).isEqualTo(ENCODED_PASSWORD);
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void givenDisabledUser_whenLoadUserByUsername_thenReturnsDisabledUser() {
        when(jpaUserRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user(false, "USER")));

        UserDetails result = userDetailsServiceAdapter.loadUserByUsername(USERNAME);

        assertThat(result.isEnabled()).isFalse();
    }

    @Test
    void givenUnknownUser_whenLoadUserByUsername_thenThrowsUsernameNotFound() {
        when(jpaUserRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsServiceAdapter.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: ghost");
    }

    private static UserEntity user(boolean enabled, String roleName) {
        RoleEntity role = new RoleEntity();
        role.setRoleName(roleName);

        UserEntity user = new UserEntity();
        user.setUsername(USERNAME);
        user.setPassword(ENCODED_PASSWORD);
        user.setEnabled(enabled);
        user.setRole(role);
        return user;
    }
}
