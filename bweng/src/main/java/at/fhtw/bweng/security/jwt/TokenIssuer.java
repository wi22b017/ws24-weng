package at.fhtw.bweng.security.jwt;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface TokenIssuer {

    String issue(UUID id, String username, String role);
}
