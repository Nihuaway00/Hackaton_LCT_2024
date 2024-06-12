package pixels.pro.fit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pixels.pro.fit.dao.entity.ChatMessage;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
