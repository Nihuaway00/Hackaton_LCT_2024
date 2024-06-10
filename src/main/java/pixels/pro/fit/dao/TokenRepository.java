package pixels.pro.fit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pixels.pro.fit.dao.entity.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    public Optional<Token> findByRefreshToken(String refreshToken);
}
