package br.bieniek.com.websocket.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JsonWebTokenProvider implements TokenProvider{

    private final KeyProvider keyProvider;

    @Override
    public Map<String, String> decode(String token) {
        DecodedJWT jwt = JWT.decode(token);
        RSAPublicKey publicKey = (RSAPublicKey) keyProvider.getPublicKey(jwt.getKeyId());
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        algorithm.verify(jwt);
        boolean expired = jwt.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).isBefore(ZonedDateTime.now());
        if (expired) throw new RuntimeException("Token expired");
        return Map.of(
                "id", jwt.getSubject(),
                "name", jwt.getClaim("name").asString(),
                "picture", jwt.getClaim("picture").asString()
        );
    }
}
