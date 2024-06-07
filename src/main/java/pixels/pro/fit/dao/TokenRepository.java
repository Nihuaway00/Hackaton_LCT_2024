package pixels.pro.fit.dao;

import org.springframework.stereotype.Repository;
import pixels.pro.fit.dao.entity.Token;
import pixels.pro.fit.dao.entity.UserProfile;

import java.util.Optional;

@Repository
public interface TokenRepository extends EntityRepository<Token> {
    public Optional<Token> findByRefreshToken(String refreshToken);
    public Optional<Token> findByAccessToken(String accessToken);
    public Optional<Token> findByUserId(Long id);

}
