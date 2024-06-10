package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.UserPrincipalRepository;
import pixels.pro.fit.dao.entity.UserPrincipal;

import java.util.Optional;

@Service
public class UserPrincipalService implements UserDetailsService {
    @Autowired
    private UserPrincipalRepository repository;

    public void save(UserPrincipal entity) {
        this.repository.save(entity);
    }

    public Optional<UserPrincipal> findById(Long id) {
        return this.repository.findById(id);
    }

    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    public Optional<UserPrincipal> findByUsername(String username){
        return repository.findByUsername(username);
    }

    public UserPrincipal getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return loadUserByUsername(username);
    }


    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }
}
