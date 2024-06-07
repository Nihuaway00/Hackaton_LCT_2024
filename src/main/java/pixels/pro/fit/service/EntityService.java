package pixels.pro.fit.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.EntityRepository;
import pixels.pro.fit.dao.entity.UserProfile;

import java.util.Optional;


interface EntityService<T> {

    public void save(T entity);
    public Optional<T> findById(Long id);

    public void deleteById(Long id);
}
