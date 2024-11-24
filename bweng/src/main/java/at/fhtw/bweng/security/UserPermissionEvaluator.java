package at.fhtw.bweng.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPermissionEvaluator {

    public boolean canModify(Authentication authentication, UUID userId) {
        // Ensure the authenticated user's ID matches the provided user ID
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Admins can modify any user
        if (principal.getRole().equals("ADMIN")) {
            return true;
        }

        // Regular users can modify only their own data
        return principal.getId().equals(userId);
    }
}
