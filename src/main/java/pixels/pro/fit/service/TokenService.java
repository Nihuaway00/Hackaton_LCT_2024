package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.TokenRepository;
import pixels.pro.fit.dao.entity.Token;

import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    private TokenRepository repository;
    public void save(Token token){
        this.repository.save(token);
    }

    public Optional<Token> findByRefreshToken(String token){
        return this.repository.findByRefreshToken(token);
    }
}

