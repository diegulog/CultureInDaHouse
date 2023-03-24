package edu.uoc.epcsd.showcatalog.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@ToString(exclude = {"managers"})
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private Long id;
    private String name;
    private String description;
    private String image;
    @Builder.Default
    private Set<User> managers = new HashSet<>();
}
