package at.fhtw.bweng.security.jwt;

import at.fhtw.bweng.property.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class JwtVerifier {
    private final JwtProperties properties;

    public JwtVerifier(JwtProperties properties) {
        this.properties = properties;
    }

    public DecodedJWT verify(String token) {
        return JWT
                .require(Algorithm.HMAC256(properties.getSecret()))
                .build()
                .verify(token);
    }
}
