package pixels.pro.fit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pixels.pro.fit.dao.entity.UserPrincipal;

import java.util.Optional;

@Repository
public interface UserPrincipalRepository extends JpaRepository<UserPrincipal, Long> {
    Optional<UserPrincipal> findByUsername(String username);
    boolean existsByUsername(String username);
}
