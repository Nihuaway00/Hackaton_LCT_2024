package pixels.pro.fit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pixels.pro.fit.dao.entity.UserProfile;

import java.util.Optional;

@Repository
public interface UserRepository extends EntityRepository<UserProfile> {
    public Optional<UserProfile> findByEmail(String email);
}
