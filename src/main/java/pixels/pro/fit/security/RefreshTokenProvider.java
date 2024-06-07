package pixels.pro.fit.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenProvider extends TokenProvider{
    public RefreshTokenProvider(@Value("${jwt.secret.refresh}") String secretPayload, @Value("${jwt.expiration.refresh}") Integer expiration) {
        super(secretPayload, expiration);
    }
}
