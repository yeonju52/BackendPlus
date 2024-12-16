package com.lion.demo.entity;

import com.lion.demo.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "senderUid", referencedColumnName = "uid")
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipientUid", referencedColumnName = "uid")
    private User recipient;

    private String message;
    private LocalDateTime timestamp;
    private int hasRead;
}
