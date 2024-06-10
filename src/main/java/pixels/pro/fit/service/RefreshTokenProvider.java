package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshTokenProvider extends JwtService {
    public RefreshTokenProvider(@Value("${jwt.secret.refresh}") String secret, @Value("${jwt.expire.refresh}") int expire) {
        super(secret, new Date(System.currentTimeMillis() + expire));
    }
}
