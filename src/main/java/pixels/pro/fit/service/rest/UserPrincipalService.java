package pixels.pro.fit.service.rest;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.UserPrincipalRepository;
import pixels.pro.fit.dao.entity.UserPrincipal;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserPrincipalService implements UserDetailsService {
    @Autowired
    private UserPrincipalRepository repository;

    public UserPrincipal save(UserPrincipal entity) {
        return this.repository.saveAndFlush(entity);
    }

    public UserPrincipal findById(Long id) throws UsernameNotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким ID не найден"));
    }

    public void deleteById(Long id) {
        this.repository.deleteById(id);
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
