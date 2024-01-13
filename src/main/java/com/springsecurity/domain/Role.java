package com.springsecurity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springsecurity.constant.RoleName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @CreationTimestamp
    private OffsetDateTime updatedAt;

    @ManyToOne
    @JsonBackReference
    User user;

    public Role(RoleName name, User user) {
        this.name = name;
        this.user = user;
    }
}