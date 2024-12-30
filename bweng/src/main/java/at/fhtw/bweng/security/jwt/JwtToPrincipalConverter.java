package at.fhtw.bweng.security.jwt;

import at.fhtw.bweng.security.UserPrincipal;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtToPrincipalConverter {
    public UserPrincipal convert(DecodedJWT decodedJWT) {
        return new UserPrincipal(
                UUID.fromString(decodedJWT.getSubject()),
                decodedJWT.getClaim("username").asString(),
                "",
                decodedJWT.getClaim("role").asString()
        );
    }
}
