package edu.uoc.epcsd.showcatalog.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.showcatalog.application.request.CreatePerformanceRequest;
import edu.uoc.epcsd.showcatalog.application.request.CreateShowRequest;
import edu.uoc.epcsd.showcatalog.domain.Category;
import edu.uoc.epcsd.showcatalog.domain.Performance;
import edu.uoc.epcsd.showcatalog.domain.Show;
import edu.uoc.epcsd.showcatalog.domain.Status;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value=CatalogRESTController.class)
public class CatalogControllerUnitTest {
    private static final String MUSIC_SHOWS = "Music shows";
    private static final String BASKET_SHOWS = "Basket shows";
    private static final String REST_CATEGORIES_PATH = "/categories";
    private static final String REST_SHOWS_PATH = "/shows";
    private static final String REST_PERFORMANCES_PATH = "/performances";
    private static final String REST_PARAMETER_ID_PATH = "/{id}";
    private static final String SHOWS_KISS = "Kiss - The final tour";
    private static final String SHOWS_CATS = "Cats";

    // Avoid the entityManagerFactory error by forcing a custom configuration
    @Configuration
    @ComponentScan(basePackageClasses = { CatalogRESTController.class })
    public static class TestConf {}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private UserService userService;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private MailSenderService mailSenderService;

    @TestConfiguration
    static class DefaultConfigWithoutCsrf extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            super.configure(http);
            http.csrf().disable();
        }
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_categories_should_return_all() throws Exception {
        Category categoryOne = Category.builder().name(MUSIC_SHOWS).build();
        Category categoryTwo = Category.builder().name(BASKET_SHOWS).build();
        List<Category> categories = Arrays.asList(categoryOne, categoryTwo);

        Mockito.when(catalogService.findAllCategories()).thenReturn(categories);

        mockMvc.perform(get(REST_CATEGORIES_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(MUSIC_SHOWS)))
                .andExpect(jsonPath("$[1].name", is(BASKET_SHOWS)));
    }

/*    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_shows_should_return_all() throws Exception {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Show showOne = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Show showTwo = Show.builder().name(SHOWS_CATS).category(categoryMusicShow).capacity(300L).build();
        List<Show> shows = Arrays.asList(showOne, showTwo);

        Mockito.when(catalogService.findAllShows()).thenReturn(shows);

        mockMvc.perform(get(REST_SHOWS_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(SHOWS_KISS)))
                .andExpect(jsonPath("$[1].name", is(SHOWS_CATS)));
    }*/

/*    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_shows_by_name_should_return_show() throws Exception {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Show actualShow = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        List<Show> shows = List.of(actualShow);

        Mockito.when(catalogService.findShowsByName(SHOWS_KISS)).thenReturn(shows);

        mockMvc.perform(get(REST_SHOWS_PATH).param("name",SHOWS_KISS).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(SHOWS_KISS)));
    }*/

/*    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void no_find_shows_by_name_should_return_empty() throws Exception {

        Mockito.when(catalogService.findShowsByName(SHOWS_KISS)).thenReturn(null);

        mockMvc.perform(get(REST_SHOWS_PATH).param("name",SHOWS_KISS).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().doesNotExist("content-type"));
    }*/

/*    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_shows_by_category_name_should_return_shows() throws Exception {
        Long notRealId = 120L;
        Category categoryMusicShow = Category.builder().id(notRealId).name(MUSIC_SHOWS).build();
        Show showOne = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Show showTwo = Show.builder().name(SHOWS_CATS).category(categoryMusicShow).capacity(300L).build();
        List<Show> shows = Arrays.asList(showOne, showTwo);

        Mockito.when(catalogService.findShowsByCategory(notRealId)).thenReturn(shows);

        mockMvc.perform(get(REST_SHOWS_PATH).param("categoryId", String.valueOf(notRealId)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(SHOWS_KISS)))
                .andExpect(jsonPath("$[1].name", is(SHOWS_CATS)));
    }*/

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_shows_by_id_should_return_show() throws Exception {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Show actualShow = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Long actualShowId = 120L;

        Mockito.when(catalogService.findShowById(actualShowId)).thenReturn(Optional.of(actualShow));

        mockMvc.perform(get(REST_SHOWS_PATH + REST_PARAMETER_ID_PATH,actualShowId).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(SHOWS_KISS)));
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void find_shows_performances_by_id_show_should_return_performances() throws Exception {
        Performance performanceOne = Performance.builder()
                .date(LocalDate.parse("2022-05-15"))
                .time(LocalTime.parse("21:30:00"))
                .streamingURL("http://foo.bar")
                .remainingSeats(300L)
                .status(Status.CREATED)
                .build();
        Performance performanceTwo = Performance.builder()
                .date(LocalDate.parse("2022-05-18"))
                .time(LocalTime.parse("21:00:00"))
                .streamingURL("http://foo.bar")
                .remainingSeats(200L)
                .status(Status.CREATED)
                .build();
        HashSet<Performance> performances = new HashSet<>() {{
            add(performanceOne);
            add(performanceTwo);
        }};
        Long actualShowId = 120L;

        Mockito.when(catalogService.getShowPerformances(actualShowId)).thenReturn(performances);

        mockMvc.perform(get(REST_SHOWS_PATH + REST_PARAMETER_ID_PATH + REST_PERFORMANCES_PATH,actualShowId).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].date", containsInAnyOrder("2022-05-15", "2022-05-18")))
                .andExpect(jsonPath("$[*].time", containsInAnyOrder("21:30:00", "21:00:00")));
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void post_shows_should_create_show() throws Exception {
        Long notRealIdCategory = 120L;
        Long notRealIdShow = 321L;
        Category categoryMusicShow = Category.builder().id(notRealIdCategory).name(MUSIC_SHOWS).build();
        Show newShow = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();

        Mockito.when(catalogService.createShow(newShow)).thenReturn(notRealIdShow);

        CreateShowRequest showRequest = new CreateShowRequest(newShow);

        mockMvc.perform(post(REST_SHOWS_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**" + REST_SHOWS_PATH + "/" + notRealIdShow ));
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void delete_shows_by_id_should_cancel_show() throws Exception {
        Long actualShowId = 120L;

        Mockito.doNothing().when(catalogService).cancelShow(actualShowId);

        mockMvc.perform(delete(REST_SHOWS_PATH + REST_PARAMETER_ID_PATH,actualShowId).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(catalogService, VerificationModeFactory.times(1)).cancelShow(actualShowId);
        Mockito.reset(catalogService);
    }

/*    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "ADMIN_USER")
    void put_performance_shows_should_add_performance_to_show() throws Exception {
        Long actualShowId = 120L;
        Performance performanceOne = Performance.builder()
                .date(LocalDate.parse("2022-05-15"))
                .time(LocalTime.parse("21:30:00"))
                .streamingURL("http://foo.bar")
                .remainingSeats(300L)
                .status(Status.CREATED)
                .build();

        Mockito.doNothing().when(catalogService).createShowPerformance(actualShowId,performanceOne);

        CreatePerformanceRequest showRequest = new CreatePerformanceRequest(performanceOne);

        mockMvc.perform(put(REST_SHOWS_PATH + REST_PARAMETER_ID_PATH,actualShowId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        //passing the object through the call causes it not to be the same occurrence then I can only ensure the type
        Mockito.verify(catalogService, VerificationModeFactory.times(1)).createShowPerformance(Mockito.anyLong(),Mockito.any(Performance.class));
        Mockito.reset(catalogService);
    }*/

}
