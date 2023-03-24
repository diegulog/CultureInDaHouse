package edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa;

import edu.uoc.epcsd.showcatalog.domain.Role;
import lombok.*;
import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class RoleEntity implements DomainTranslatable<Role> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    public static RoleEntity fromDomain(Role role) {
        if (role == null) return null;
        return RoleEntity.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    @Override
    public Role toDomain() {
        return Role.builder()
                .id(id)
                .roleName(roleName)
                .build();
    }
}
