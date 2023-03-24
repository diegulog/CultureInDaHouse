package edu.uoc.epcsd.showcatalog.domain;

import lombok.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ToString(exclude = {"roles"})
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date lastActive;
    private Date createTime;
    private boolean active;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();


}
