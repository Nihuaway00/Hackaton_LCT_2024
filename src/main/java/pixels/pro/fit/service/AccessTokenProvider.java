package pixels.pro.fit.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccessTokenProvider extends JwtService {
    public AccessTokenProvider(@Value("${jwt.secret.access}") String secret, @Value("${jwt.expire.access}") int expire) {
        super(secret, new Date(System.currentTimeMillis() + expire));
    }
}
