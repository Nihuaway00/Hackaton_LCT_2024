package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.TokenRepository;
import pixels.pro.fit.dao.entity.Token;
import pixels.pro.fit.dao.entity.UserProfile;

import java.util.Optional;

@Service
public class TokenService implements EntityService<Token>{
    @Autowired
    private TokenRepository repository;

    public Optional<Token> findByRefreshToken(String refreshToken){
        return this.repository.findByRefreshToken(refreshToken);
    }

    public Optional<Token> findByUserId(Long id){
        return this.repository.findByUserId(id);
    }

    @Override
    public void save(Token entity) {
        this.repository.save(entity);
    }

    @Override
    public Optional<Token> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }
}
