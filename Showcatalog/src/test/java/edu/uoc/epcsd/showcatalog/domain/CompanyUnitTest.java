package edu.uoc.epcsd.showcatalog.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompanyUnitTest {
    private static final String ADMIN = "Admin";
    private static final String COMPANY = "TEST Company";

    @Test
    void should_create_company() {
        User user = User.builder()
                .id(0L)
                .username(ADMIN)
                .password(ADMIN)
                .firstName(ADMIN)
                .lastName(ADMIN)
                .build();
        Set<User> managers = new HashSet<>() {{
            add(user);
        }};

        Company company = Company.builder()
                .id(0L)
                .name(COMPANY)
                .description(COMPANY)
                .image(COMPANY)
                .managers(managers)
                .build();

        assertThat(company.getId()).isEqualTo(0L);
        assertThat(company.getName()).isEqualTo(COMPANY);
        assertThat(company.getDescription()).isEqualTo(COMPANY);
        assertThat(company.getImage()).isEqualTo(COMPANY);
        assertThat(company.getManagers()).usingRecursiveComparison().isEqualTo(managers);
    }

    @Test
    void should_edit_company() {
        User user = User.builder()
                .id(0L)
                .username(ADMIN)
                .password(ADMIN)
                .firstName(ADMIN)
                .lastName(ADMIN)
                .build();
        Set<User> managers = new HashSet<>() {{
            add(user);
        }};

        Company company = Company.builder().build();

        company.setId(0L);
        company.setName(COMPANY);
        company.setDescription(COMPANY);
        company.setImage(COMPANY);
        company.setManagers(managers);

        assertThat(company.getId()).isEqualTo(0L);
        assertThat(company.getName()).isEqualTo(COMPANY);
        assertThat(company.getDescription()).isEqualTo(COMPANY);
        assertThat(company.getImage()).isEqualTo(COMPANY);
        assertThat(company.getManagers()).usingRecursiveComparison().isEqualTo(managers);
    }

    @Test
    void should_start_without_any_default_manager() {
        Company company = Company.builder().name(COMPANY).description(COMPANY).build();
        assertThat(company.getManagers().size()).isEqualTo(0);
    }

    @Test
    void should_be_compared_and_passed_to_chain_company() {
        Company oneCompany = Company.builder().name(COMPANY).description(COMPANY).build();
        Company twoCompany = Company.builder().name(COMPANY).description(COMPANY).build();

        assertEquals(oneCompany, twoCompany);
        assertThat(oneCompany.toString()).isEqualTo(twoCompany.toString());
    }

    @Test
    void should_begin_company_with_empty_values() {
        Company oneCompany = new Company();
        HashSet<User> managers = new HashSet<>();
        assertEquals(managers , oneCompany.getManagers());
    }

}
