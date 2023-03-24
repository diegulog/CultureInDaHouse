package edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uoc.epcsd.showcatalog.domain.User;
import lombok.*;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity implements DomainTranslatable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "last_active")
    private Date lastActive;
    @Column(name = "create_time", nullable = false)
    private Date createTime;
    @Column(name = "active", nullable = false)
    private Boolean active;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    //@JsonIgnore
    private Set<RoleEntity> roles = new HashSet<>();

    @Override
    public User toDomain() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .lastActive(lastActive)
                .createTime(createTime)
                .active(active)
                .roles(roles.stream().map(RoleEntity::toDomain).collect(Collectors.toSet()))
                .build();
    }

    public static UserEntity fromDomain(User user) {
        if (user == null) return null;
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .lastActive(user.getLastActive())
                .createTime(user.getCreateTime())
                .active(user.isActive())
                .roles(user.getRoles().stream().map(RoleEntity::fromDomain).collect(Collectors.toSet()))
                .build();
    }
}
