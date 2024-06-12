package pixels.pro.fit.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pixels.pro.fit.dao.MessageStatus;

import java.util.Date;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    private UserPrincipal sender;
    private String senderName;

    @OneToOne(fetch = FetchType.LAZY)
    private UserPrincipal recipient;
    private String recipientName;

    private String content;
    private Date timestamp;
    private MessageStatus status;
}
