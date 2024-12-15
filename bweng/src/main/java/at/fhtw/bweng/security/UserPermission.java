package at.fhtw.bweng.security;

import at.fhtw.bweng.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPermission implements AccessPermission {
    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(User.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, UUID resourceId) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Check if the user is an ADMIN or their ID matches the resource ID
        return principal.getRole().equals("ADMIN") || principal.getId().equals(resourceId);
    }
}