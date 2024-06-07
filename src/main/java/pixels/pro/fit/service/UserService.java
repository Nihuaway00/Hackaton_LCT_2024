package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.EntityRepository;
import pixels.pro.fit.dao.UserRepository;
import pixels.pro.fit.dao.entity.Password;
import pixels.pro.fit.dao.entity.UserProfile;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements EntityService<UserProfile>{
    @Autowired
    private UserRepository repository;
    public List<UserProfile> findAll(){
        return this.repository.findAll();
    }

    public Optional<UserProfile> findByEmail(String email){
        return this.repository.findByEmail(email);
    }


    @Override
    public void save(UserProfile entity) {
        this.repository.save(entity);
    }

    @Override
    public Optional<UserProfile> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }
}
