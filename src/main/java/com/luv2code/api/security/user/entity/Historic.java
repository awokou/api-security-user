package com.luv2code.api.security.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "historic")
public class Historic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_historic_user_id"), nullable = false)
    private User user;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(nullable = false,name = "login_time")
    private Instant loginTime;
}
