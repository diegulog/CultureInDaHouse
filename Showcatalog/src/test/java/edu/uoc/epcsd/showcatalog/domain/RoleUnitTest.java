package edu.uoc.epcsd.showcatalog.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleUnitTest {
    private static final String ADMIN_USER = "ADMIN_USER";

    @Test
    void should_create_role() {
        Role actualRole = Role.builder()
                .id(0L)
                .roleName(ADMIN_USER)
                .build();

        assertThat(actualRole.getId()).isEqualTo(0L);
        assertThat(actualRole.getRoleName()).isEqualTo(ADMIN_USER);
    }

    @Test
    void should_edit_role() {
        Role actualRole = Role.builder().build();

        actualRole.setId(0L);
        actualRole.setRoleName(ADMIN_USER);

        assertThat(actualRole.getId()).isEqualTo(0L);
        assertThat(actualRole.getRoleName()).isEqualTo(ADMIN_USER);
    }


    @Test
    void should_be_compared_and_passed_to_chain_role() {
        Role oneRole = Role.builder().id(0L).roleName(ADMIN_USER).build();
        Role twoRole = Role.builder().id(0L).roleName(ADMIN_USER).build();

        assertEquals(oneRole, twoRole);
        assertThat(oneRole.toString()).isEqualTo(twoRole.toString());
    }

    @Test
    void should_begin_role_with_empty_values() {
        Role oneRole = new Role();
        assertEquals(oneRole.getId(), null);
    }


}
