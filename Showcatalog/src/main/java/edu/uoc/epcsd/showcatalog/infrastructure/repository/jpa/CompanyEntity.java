package edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa;

import edu.uoc.epcsd.showcatalog.domain.Company;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class CompanyEntity implements DomainTranslatable<Company>  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "image", nullable = false)
    private String image;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "company_user", joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))

    private Set<UserEntity> managers = new HashSet<>();

    public static CompanyEntity fromDomain(Company company) {
        if (company == null) {
            return null;
        }
        return CompanyEntity.builder()
                .name(company.getName())
                .description(company.getDescription())
                .image(company.getImage())
                .managers(company.getManagers().stream().map(UserEntity::fromDomain).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public Company toDomain() {
        return Company.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .image(this.getImage())
                .managers(managers.stream().map(UserEntity::toDomain).collect(Collectors.toSet()))
                .build();
    }
}
