package pixels.pro.fit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pixels.pro.fit.dao.entity.ChatRoom;
import pixels.pro.fit.dao.entity.UserPrincipal;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    public Optional<ChatRoom> findBySenderIdAndRecipientId(UserPrincipal sender,UserPrincipal recipient);
}
