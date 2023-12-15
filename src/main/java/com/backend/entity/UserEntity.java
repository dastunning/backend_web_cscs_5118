package com.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class UserEntity { //Users table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    private String name;

    @Column(columnDefinition = "varchar default 'USER'")
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;

    private boolean enabled;

    public enum Role implements GrantedAuthority {
        USER, ADMIN;

        @Override
        public String getAuthority() {
            return name();
        }
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
