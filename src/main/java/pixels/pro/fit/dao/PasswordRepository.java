package pixels.pro.fit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pixels.pro.fit.dao.entity.Password;
import pixels.pro.fit.dao.entity.UserProfile;

@Repository
public interface PasswordRepository extends EntityRepository<Password> {
}
