package edu.uoc.epcsd.showcatalog.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserUnitTest {
    private static final String ADMIN = "Admin";

    @Test
    void should_create_user() {
        Date date = Date.from(LocalDate.parse("2022-05-18").atStartOfDay(ZoneId.systemDefault()).toInstant());
        Role role = Role.builder()
                .id(0L)
                .roleName("ADMIN_USER")
                .build();
        HashSet<Role> roles = new HashSet<>() {{
            add(role);
        }};

        User actualUser = User.builder()
                .id(0L)
                .username(ADMIN)
                .password(ADMIN)
                .firstName(ADMIN)
                .lastName(ADMIN)
                .lastActive(date)
                .createTime(date)
                .active(true)
                .roles(roles)
                .build();

        assertThat(actualUser.getId()).isEqualTo(0L);
        assertThat(actualUser.getUsername()).isEqualTo(ADMIN);
        assertThat(actualUser.getPassword()).isEqualTo(ADMIN);
        assertThat(actualUser.getFirstName()).isEqualTo(ADMIN);
        assertThat(actualUser.getLastName()).isEqualTo(ADMIN);
        assertThat(actualUser.getLastActive()).isEqualTo(date);
        assertThat(actualUser.getCreateTime()).isEqualTo(date);
        assertThat(actualUser.isActive()).isEqualTo(true);
        assertThat(actualUser.getRoles()).usingRecursiveComparison().isEqualTo(roles);
    }


    @Test
    void should_edit_user() {
        Date date = Date.from(LocalDate.parse("2022-05-18").atStartOfDay(ZoneId.systemDefault()).toInstant());        Role role = Role.builder()
                .id(0L)
                .roleName("ADMIN_USER")
                .build();
        HashSet<Role> roles = new HashSet<>() {{
            add(role);
        }};

        User actualUser = User.builder().build();

        actualUser.setId(0L);
        actualUser.setUsername(ADMIN);
        actualUser.setPassword(ADMIN);
        actualUser.setFirstName(ADMIN);
        actualUser.setLastName(ADMIN);
        actualUser.setLastActive(date);
        actualUser.setCreateTime(date);
        actualUser.setActive(true);
        actualUser.setRoles(roles);

        assertThat(actualUser.getId()).isEqualTo(0L);
        assertThat(actualUser.getUsername()).isEqualTo(ADMIN);
        assertThat(actualUser.getPassword()).isEqualTo(ADMIN);
        assertThat(actualUser.getFirstName()).isEqualTo(ADMIN);
        assertThat(actualUser.getLastName()).isEqualTo(ADMIN);
        assertThat(actualUser.getLastActive()).isEqualTo(date);
        assertThat(actualUser.getCreateTime()).isEqualTo(date);
        assertThat(actualUser.isActive()).isEqualTo(true);
        assertThat(actualUser.getRoles()).usingRecursiveComparison().isEqualTo(roles);
    }

    @Test
    void should_start_without_any_default_role() {
        User actualUser = User.builder().username(ADMIN).lastName(ADMIN).build();
        assertThat(actualUser.getRoles().size()).isEqualTo(0);
    }

    @Test
    void should_be_compared_and_passed_to_chain_user() {
        User oneUser = User.builder().username(ADMIN).lastName(ADMIN).build();
        User twoUser = User.builder().username(ADMIN).lastName(ADMIN).build();

        assertEquals(oneUser, twoUser);
        assertThat(oneUser.toString()).isEqualTo(twoUser.toString());
    }

    @Test
    void should_begin_user_with_empty_values() {
        User oneUser = new User();
        HashSet<Role> roles = new HashSet<>();
        assertEquals(roles , oneUser.getRoles());
    }

}
