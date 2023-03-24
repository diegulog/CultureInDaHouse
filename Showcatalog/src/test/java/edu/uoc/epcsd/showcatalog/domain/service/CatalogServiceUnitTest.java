package edu.uoc.epcsd.showcatalog.domain.service;

import edu.uoc.epcsd.showcatalog.domain.Category;
import edu.uoc.epcsd.showcatalog.domain.Performance;
import edu.uoc.epcsd.showcatalog.domain.Show;
import edu.uoc.epcsd.showcatalog.domain.Status;
import edu.uoc.epcsd.showcatalog.domain.repository.CategoryRepository;
import edu.uoc.epcsd.showcatalog.domain.repository.ShowRepository;
import edu.uoc.epcsd.showcatalog.infrastructure.kafka.KafkaConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CatalogServiceUnitTest {
    private static final String MUSIC_SHOWS = "Music shows";
    private static final String MUSIC_THEATER = "Theater shows";
    private static final String SHOWS_KISS = "Kiss - The final tour";
    private static final String SHOWS_CATS = "Cats";

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ShowRepository showRepository;

    @MockBean
    private KafkaTemplate<String, Show> kafkaTemplate;

    @Autowired
    private CatalogService catalogService;

    public CatalogServiceUnitTest() {
    }
    @Test
    void should_find_all_categories() {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Category categoryMusicTheater = Category.builder().name(MUSIC_THEATER).build();
        List<Category> categories = Arrays.asList(categoryMusicShow, categoryMusicTheater);
        Mockito.when(categoryRepository.findAllCategories()).thenReturn(categories);

        List<Category> categoriesReturn = catalogService.findAllCategories();
        assertThat(categoriesReturn).usingRecursiveComparison().isEqualTo(categories);
    }

    @Test
    void should_find_category_by_id() {
        Long notRealIdCategory = 120L;
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();

        Mockito.when(categoryRepository.findCategoryById(notRealIdCategory)).thenReturn(Optional.of(categoryMusicShow));

        Category categoryReturn = catalogService.findCategoryById(notRealIdCategory).get();

        assertThat(categoryReturn).usingRecursiveComparison().isEqualTo(categoryMusicShow);
    }

    @Test
    void should_create_category() {
        Long notRealIdCategory = 120L;
        Category newCategory = Category.builder().name(MUSIC_SHOWS).build();
        Mockito.when(categoryRepository.createCategory(newCategory)).thenReturn(notRealIdCategory);

        Long idCategoryCreate = catalogService.createCategory(newCategory);
        assertThat(idCategoryCreate).isEqualTo(notRealIdCategory);
    }

    @Test
    void should_delete_category() {
        Long notRealIdCategory = 120L;
        Mockito.doNothing().when(categoryRepository).deleteCategory(notRealIdCategory);

        catalogService.deleteCategory(notRealIdCategory);
        // we verify that it has been called once on deleteCategory and once on deleteCategory
        Mockito.verify(categoryRepository, VerificationModeFactory.times(1)).deleteCategory(notRealIdCategory);
        Mockito.reset(categoryRepository);
    }

    @Test
    void should_find_all_shows() {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Show showOne = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Show showTwo = Show.builder().name(SHOWS_CATS).category(categoryMusicShow).capacity(300L).build();
        List<Show> shows = Arrays.asList(showOne, showTwo);
        Mockito.when(showRepository.findAllShows()).thenReturn(shows);

        List<Show> showsReturn = catalogService.findAllShows();
        assertThat(showsReturn).usingRecursiveComparison().isEqualTo(shows);
    }

    @Test
    void should_not_find_all_shows() {
        Mockito.when(showRepository.findAllShows()).thenReturn(null);

        List<Show> showsReturn= catalogService.findAllShows();
        assertThat(showsReturn).isNull();
    }

    @Test
    void should_find_show_by_id() {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Show actualShow = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Long actualShowId = 120L;
        Mockito.when(showRepository.findShowById(actualShowId)).thenReturn(Optional.of(actualShow));

        Show show = catalogService.findShowById(actualShowId).get();
        assertThat(show).usingRecursiveComparison().isEqualTo(actualShow);
    }

    @Test
    void should_not_find_show_by_id() {
        Long notRealId = 120L;
        Mockito.when(showRepository.findShowById(notRealId)).thenReturn(null);

        Optional<Show> show = catalogService.findShowById(notRealId);
        assertThat(show).isNull();
    }

    @Test
    void should_find_show_by_name() {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Show actualShow = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        List<Show> shows = List.of(actualShow);
        Mockito.when(showRepository.findShowsByName(SHOWS_KISS)).thenReturn(shows);

        List<Show> showsReturn = catalogService.findShowsByName(SHOWS_KISS);
        assertThat(showsReturn).usingRecursiveComparison().isEqualTo(shows);
    }

    @Test
    void should_not_find_show_by_name() {
        Mockito.when(showRepository.findShowsByName(SHOWS_KISS)).thenReturn(null);

        List<Show> showsReturn= catalogService.findShowsByName(SHOWS_KISS);
        assertThat(showsReturn).isNull();
    }

    @Test
    void should_find_show_by_category() {
        Long notRealId = 120L;
        Category categoryMusicShow = Category.builder().id(notRealId).name(MUSIC_SHOWS).build();
        Show showOne = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Show showTwo = Show.builder().name(SHOWS_CATS).category(categoryMusicShow).capacity(300L).build();
        List<Show> shows = Arrays.asList(showOne, showTwo);
        Mockito.when(showRepository.findShowsByCategory(notRealId)).thenReturn(shows);

        List<Show> showsReturn = catalogService.findShowsByCategory(notRealId);
        assertThat(showsReturn).usingRecursiveComparison().isEqualTo(shows);
    }

    @Test
    void should_not_find_show_by_category() {
        Long notRealId = 120L;
        Mockito.when(showRepository.findShowsByCategory(notRealId)).thenReturn(null);

        List<Show> showsReturn= catalogService.findShowsByCategory(notRealId);
        assertThat(showsReturn).isNull();
    }

    @Test
    void should_find_show_performances_by_id_show() {
        Category categoryMusicShow = Category.builder().name(MUSIC_SHOWS).build();
        Performance performanceOne = Performance.builder()
                .date(LocalDate.parse("2022-05-15"))
                .time(LocalTime.parse("21:30"))
                .streamingURL("http://foo.bar")
                .remainingSeats(300L)
                .status(Status.CREATED)
                .build();
        Performance performanceTwo = Performance.builder()
                .date(LocalDate.parse("2022-05-18"))
                .time(LocalTime.parse("21:30"))
                .streamingURL("http://foo.bar")
                .remainingSeats(300L)
                .status(Status.CREATED)
                .build();
        HashSet<Performance> performances = new HashSet<>() {{
            add(performanceOne);
            add(performanceTwo);
        }};
        Show actualShow = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).performances(performances).capacity(45000L).build();
        Long actualShowId = 120L;
        Mockito.when(showRepository.findShowById(actualShowId)).thenReturn(Optional.of(actualShow));

        Set<Performance> performancesShow = catalogService.getShowPerformances(actualShowId);
        assertThat(performancesShow).usingRecursiveComparison().isEqualTo(actualShow.getPerformances());
    }

    @Test
    void should_not_find_show_performances_by_id_show() {
        Long notRealId = 120L;
        Mockito.when(showRepository.findShowById(notRealId)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> catalogService.getShowPerformances(notRealId));

        assertThat(exception).isInstanceOf(NullPointerException.class);
        Mockito.verify(showRepository, VerificationModeFactory.times(1)).findShowById(notRealId);
        Mockito.reset(showRepository);

    }

    @Test
    void should_create_show() {
        Long notRealIdCategory = 120L;
        Long notRealIdShow = 321L;
        Category categoryMusicShow = Category.builder().id(notRealIdCategory).name(MUSIC_SHOWS).build();
        Show newShow = Show.builder().name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Show newCreate =Show.builder().id(notRealIdShow).name(SHOWS_KISS).category(categoryMusicShow).capacity(45000L).build();
        Mockito.when(categoryRepository.findCategoryById(notRealIdCategory)).thenReturn(Optional.of(categoryMusicShow));
        Mockito.when(showRepository.createShow(newShow)).thenReturn(notRealIdShow);
        Mockito.when(showRepository.findShowById(notRealIdShow)).thenReturn(Optional.of(newCreate));

        SettableListenableFuture<SendResult<String, Show>> returnKafka = new SettableListenableFuture<>();
        Mockito.when(kafkaTemplate.send(KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD,newCreate)).thenReturn(returnKafka);

        Long idShowCreate = catalogService.createShow(newShow);
        assertThat(idShowCreate).isEqualTo(notRealIdShow);
        // we verify that kafka has been called once from the createShow of the service
        Mockito.verify(kafkaTemplate, VerificationModeFactory.times(1)).send(Mockito.anyString(),Mockito.any(Show.class));
        Mockito.reset(kafkaTemplate);
    }

    @Test
    void should_cancel_show() {
        Long notRealIdShow = 321L;
        Show newShow = Show.builder().id(notRealIdShow).name(SHOWS_KISS).capacity(45000L).build();
        Mockito.when(showRepository.findShowById(notRealIdShow)).thenReturn(Optional.of(newShow));
        Mockito.doNothing().when(showRepository).updateShow(newShow);

        catalogService.cancelShow(notRealIdShow);
        // we verify that it has been called once on findShowById and once on updateShow
        Mockito.verify(showRepository, VerificationModeFactory.times(1)).findShowById(notRealIdShow);
        Mockito.verify(showRepository, VerificationModeFactory.times(1)).updateShow(newShow);
        Mockito.reset(showRepository);
    }

    @Test()
    void should_give_throw_when_cancel_show_not_exist() {
        Long notRealIdShow = 321L;
        Mockito.when(showRepository.findShowById(notRealIdShow)).thenReturn(null);
        //Mockito.doThrow(NullPointerException.class).when(showRepository).updateShow(null);

        Exception exception = assertThrows(RuntimeException.class, () -> catalogService.cancelShow(notRealIdShow));

        assertThat(exception).isInstanceOf(NullPointerException.class);
        Mockito.verify(showRepository, VerificationModeFactory.times(1)).findShowById(notRealIdShow);
        Mockito.reset(showRepository);
    }

    @Test
    void should_add_performance_to_show() {
        Long notRealIdShow = 321L;
        Performance performanceOne = Performance.builder()
                .date(LocalDate.parse("2022-05-15"))
                .time(LocalTime.parse("21:30"))
                .streamingURL("http://foo.bar")
                .remainingSeats(300L)
                .status(Status.CREATED)
                .build();

        Mockito.doNothing().when(showRepository).addShowPerformance(notRealIdShow, performanceOne);

        catalogService.createShowPerformance(notRealIdShow,performanceOne);
        // we verify that it has been called once on addShowPerformance
        Mockito.verify(showRepository, VerificationModeFactory.times(1)).addShowPerformance(notRealIdShow, performanceOne);
        Mockito.reset(showRepository);
    }
}
