package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.PasswordRepository;
import pixels.pro.fit.dao.entity.Password;

import java.util.Optional;

@Service
public class PasswordService implements EntityService<Password>{
    @Autowired
    private PasswordRepository repository;
    public void deleteByUserId(Long id){
        this.repository.deleteById(id);
    }

    @Override
    public void save(Password entity) {
        this.repository.save(entity);
    }

    @Override
    public Optional<Password> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }
}
