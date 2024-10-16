package com.meet.bot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String telegramId;

    @Column
    private String username;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String systemName;

    @Column(nullable = false)
    private Boolean blocked = false;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "users")
    private Set<ChatRoom> chatRooms;
}
