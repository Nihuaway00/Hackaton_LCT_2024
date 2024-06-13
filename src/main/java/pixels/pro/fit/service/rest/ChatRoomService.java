package pixels.pro.fit.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.ChatRoomRepository;
import pixels.pro.fit.dao.entity.ChatRoom;
import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.exception.NoChatFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository repository;

    public ChatRoom create(List<UserPrincipal> members){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setMembers(members);
        return this.repository.save(chatRoom);
    }

    public Optional<ChatRoom> findByMembers(List<UserPrincipal> members) {
        return this.repository.findByMembersContains(members);
    }
}
