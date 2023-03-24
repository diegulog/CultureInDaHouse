package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import edu.uoc.epcsd.showcatalog.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceUnitTest {
    private static final String ADMIN = "Admin";
    private static final String TEST = "Test";
    private static final String ROLE = "ADMIN_USER";

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void should_find_all_users() {
        User adminUser = User.builder().id(1L).username(ADMIN).build();
        User testUser = User.builder().id(2L).username(TEST).build();
        List<User> users = Arrays.asList(adminUser, testUser);
        Mockito.when(userRepository.findAllUsers()).thenReturn(users);

        List<User> usersReturn = userService.findAllUsers();
        assertThat(usersReturn).usingRecursiveComparison().isEqualTo(users);
    }

    @Test
    void should_find_user_by_id() {
        Long notRealIdUser = 1L;
        User adminUser = User.builder().id(notRealIdUser).username(ADMIN).build();
        Mockito.when(userRepository.findUserById(notRealIdUser)).thenReturn(Optional.of(adminUser));

        User usersReturn = userService.findUserById(notRealIdUser).get();
        assertThat(usersReturn).usingRecursiveComparison().isEqualTo(adminUser);
    }

    @Test
    void should_find_user_by_name() {
        User adminUser = User.builder().id(1L).username(ADMIN).build();
        Mockito.when(userRepository.findUserByUsername(ADMIN)).thenReturn(Optional.of(adminUser));

        User usersReturn = userService.findUserByUsername(ADMIN).get();
        assertThat(usersReturn).usingRecursiveComparison().isEqualTo(adminUser);
    }

    @Test
    void should_create_user() {
        Long notRealIdUser = 1L;
        Date date = Date.from(LocalDate.parse("2022-05-18").atStartOfDay(ZoneId.systemDefault()).toInstant());
        Role role = Role.builder()
                .id(0L)
                .roleName(ROLE)
                .build();
        HashSet<Role> roles = new HashSet<>() {{
            add(role);
        }};
        User actualUser = User.builder()
                .username(ADMIN)
                .password(ADMIN)
                .firstName(ADMIN)
                .lastName(ADMIN)
                .lastActive(date)
                .createTime(date)
                .active(true)
                .roles(roles)
                .build();

        Mockito.when(userRepository.createUser(actualUser)).thenReturn(notRealIdUser);

        Long idUserCreate = userService.createUser(actualUser);
        assertThat(idUserCreate).isEqualTo(notRealIdUser);
    }

    @Test
    void should_not_create_a_user_that_exists() {
        User adminUser = User.builder().username(ADMIN).build();

        Mockito.when(userRepository.findUserByUsername(ADMIN)).thenReturn(Optional.of(adminUser));

        Long idUserCreate = userService.createUser(adminUser);
        assertThat(idUserCreate).isEqualTo(null);
    }
    @Test
    void should_delete_user() {
        Long notRealIdUser = 1L;
        Mockito.doNothing().when(userRepository).deleteUser(notRealIdUser);

        userService.deleteUser(notRealIdUser);
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).deleteUser(notRealIdUser);
        Mockito.reset(userRepository);
    }

    @Test
    void should_find_role_by_name() {
        Role role = Role.builder().id(0L).roleName(ROLE).build();
        Mockito.when(userRepository.findRoleByName(ROLE)).thenReturn(Optional.of(role));

        Role roleReturn = userService.findRoleByName(ROLE).get();
        assertThat(roleReturn).usingRecursiveComparison().isEqualTo(role);
    }


    @Test
    void should_not_load_a_user_that_exists() {

        Exception exception = assertThrows(RuntimeException.class, () -> userService.loadUserByUsername(ADMIN));

        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findUserByUsername(ADMIN);
        Mockito.reset(userRepository);

    }

}
