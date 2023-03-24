package edu.uoc.epcsd.showcatalog.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.showcatalog.application.request.CreateUserRequest;
import edu.uoc.epcsd.showcatalog.domain.Role;
import edu.uoc.epcsd.showcatalog.domain.User;
import edu.uoc.epcsd.showcatalog.domain.service.CatalogService;
import edu.uoc.epcsd.showcatalog.domain.service.CompanyService;
import edu.uoc.epcsd.showcatalog.domain.service.MailSenderService;
import edu.uoc.epcsd.showcatalog.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value=UserRESTController.class)
public class UserControllerUnitTest {
    private static final String ADMIN = "Admin";
    private static final String TEST = "Test";
    private static final String PASS = "AdMin01.";
    private static final String PASS_ENCODE = "2JHF83H";
    private static final String ROLE = "STANDARD_USER";

    private static final String REST_USERS_PATH = "/users";
    private static final String REST_ME_PATH = "/me";
    private static final String REST_USERNAME_PATH = "/username";
    private static final String REST_PARAMETER_ID_PATH = "/{id}";
    private static final String REST_PARAMETER_USERNAME_PATH = "/{username}";

    // Avoid the entityManagerFactory error by forcing a custom configuration
    @Configuration
    @ComponentScan(basePackageClasses = { UserRESTController.class })
    public static class TestConf {}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private MailSenderService mailSenderService;

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_users_should_return_all() throws Exception {
        User adminUser = User.builder().id(1L).username(ADMIN).build();
        User testUser = User.builder().id(2L).username(TEST).build();
        List<User> users = Arrays.asList(adminUser, testUser);

        Mockito.when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get(REST_USERS_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(ADMIN)))
                .andExpect(jsonPath("$[1].username", is(TEST)));
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_users_by_id_should_return_user() throws Exception {
        Long userId = 1L;
        User adminUser = User.builder().id(userId).username(ADMIN).build();

        Mockito.when(userService.findUserById(userId)).thenReturn(Optional.of(adminUser));

        mockMvc.perform(get(REST_USERS_PATH + REST_PARAMETER_ID_PATH,userId).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(ADMIN)));
    }

    @Test
    @WithMockUser(username = ADMIN, password = "Admin", roles = "ADMIN_USER")
    void find_me_should_return_my_user() throws Exception {
        User adminUser = User.builder().id(1L).username(ADMIN).build();

        Mockito.when(userService.findUserByUsername(ADMIN)).thenReturn(Optional.of(adminUser));

        mockMvc.perform(get(REST_USERS_PATH + REST_ME_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(ADMIN)));
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_users_by_username_should_return_user() throws Exception {
        User adminUser = User.builder().id(1L).username(ADMIN).password(ADMIN).build();

        Mockito.when(userService.findUserByUsername(ADMIN)).thenReturn(Optional.of(adminUser));

        mockMvc.perform(get(REST_USERNAME_PATH + REST_PARAMETER_USERNAME_PATH,ADMIN).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(ADMIN)));
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void post_user_should_create_user() throws Exception {
        Long userId = 0L;
        User testUser = User.builder().username(TEST).password(PASS).build();
        Role role = Role.builder().id(2L).roleName(ROLE).build();
        User testUserEncode = User.builder().username(TEST).password(PASS_ENCODE).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        testUserEncode.setRoles(roles);

        Mockito.when(bCryptPasswordEncoder.encode(PASS)).thenReturn(PASS_ENCODE);
        Mockito.when(userService.findRoleByName(ROLE)).thenReturn(Optional.of(role));
        Mockito.when(userService.createUser(testUser)).thenReturn(userId);

        CreateUserRequest userRequest = new CreateUserRequest(testUser);

        mockMvc.perform(post(REST_USERS_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**" + REST_USERS_PATH + "/" + userId));

        Mockito.verify(bCryptPasswordEncoder, VerificationModeFactory.times(1)).encode(PASS);
        Mockito.reset(bCryptPasswordEncoder);
        Mockito.verify(userService, VerificationModeFactory.times(1)).findRoleByName(ROLE);
        Mockito.verify(userService, VerificationModeFactory.times(1)).createUser(testUserEncode);
        Mockito.reset(userService);
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void delete_user_by_id_should_delete() throws Exception {
        Long userId = 0L;

        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete(REST_USERS_PATH + REST_PARAMETER_ID_PATH,userId).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, VerificationModeFactory.times(1)).deleteUser(userId);
        Mockito.reset(userService);
    }

}
