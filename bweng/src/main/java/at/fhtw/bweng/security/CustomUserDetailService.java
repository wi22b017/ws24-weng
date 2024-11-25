package at.fhtw.bweng.security;

import at.fhtw.bweng.model.User;
import at.fhtw.bweng.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByUsername(usernameOrEmail);

            if (!"ACTIVE".equals(user.getStatus())) {
                throw new IllegalStateException("User account is not active");
            }

            return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
        } catch (NoSuchElementException e) {
            try {
                User user = userService.getUserByEmail(usernameOrEmail);

                if (!"ACTIVE".equals(user.getStatus())) {
                    throw new IllegalStateException("User account is not active");
                }

                return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
            } catch (NoSuchElementException emailException) {
                throw new NoSuchElementException("User not found with username or email: " + usernameOrEmail);
            }
        }
    }
}
