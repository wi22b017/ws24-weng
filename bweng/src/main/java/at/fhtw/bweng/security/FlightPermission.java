package at.fhtw.bweng.security;

import at.fhtw.bweng.model.Flight;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

//Currently no used
@Component
public class FlightPermission implements AccessPermission {
    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(Flight.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, UUID resourceId) {
        return ((UserPrincipal) authentication.getPrincipal()).getId().equals(resourceId);
    }
}
