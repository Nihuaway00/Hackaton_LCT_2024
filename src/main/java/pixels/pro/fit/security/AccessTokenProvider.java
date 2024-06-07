package pixels.pro.fit.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenProvider extends TokenProvider {

    public AccessTokenProvider(@Value("${jwt.secret.access}") String secretPayload, @Value("${jwt.expiration.access}") Integer expiration) {
        super(secretPayload, expiration);
    }
}
