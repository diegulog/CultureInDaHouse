package edu.uoc.epcsd.showcatalog.infrastructure;

import edu.uoc.epcsd.showcatalog.domain.Category;
import edu.uoc.epcsd.showcatalog.domain.Performance;
import edu.uoc.epcsd.showcatalog.domain.Show;
import edu.uoc.epcsd.showcatalog.domain.Status;
import edu.uoc.epcsd.showcatalog.domain.repository.CategoryRepository;
import edu.uoc.epcsd.showcatalog.domain.repository.ShowRepository;
import edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa.CategoryEntity;
import edu.uoc.epcsd.showcatalog.infrastructure.repository.jpa.ShowEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan
//@ContextConfiguration(classes = TestConfig.class)
public class CategoryRepositoryIntegrationTest {
    private static final String MUSIC_SHOWS = "Music shows";
    private static final String SHOWS_KISS = "Kiss - The final tour";
    private static final String SHOWS_METALLICA = "Metallica - WorldWired Tour";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShowRepository showRepository;

    @Test
    void whenFindByName_thenReturnCategory() {
        String categoryName = "Music Shows";
        Category category = Category.builder().name(categoryName).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);

        entityManager.persistAndFlush(categoryEntity);

        Category fromDb = categoryRepository.findCategoryById(categoryEntity.getId()).get();
        assertThat(fromDb.getName()).isEqualTo(category.getName());
    }

    @Test
    void whenFindAllShows_thenReturnShows() {
        Category category = Category.builder().name(MUSIC_SHOWS).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        entityManager.persistAndFlush(categoryEntity);

        Show showOne = Show.builder()
                .name(SHOWS_KISS)
                .description("The band's final concert")
                .image("https://en.wikipedia.org/wiki/End_of_the_Road_World_Tour#/media/File:KISS_EndoftheRoadTourPromo.jpg")
                .duration(120.0)
                .capacity(45000L)
                .onSaleDate(LocalDate.parse("2022-05-18")).build();
        ShowEntity showEntityOne = ShowEntity.fromDomain(showOne);
        showEntityOne.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntityOne);

        Show showTwo = Show.builder()
                .name(SHOWS_METALLICA)
                .description("WorldWired Tour")
                .image("https://mariskalrock.com/wp-content/uploads/2022/02/metallica-cartel-gira-22-vegas.jpgjpg")
                .duration(120.0)
                .capacity(50000L)
                .onSaleDate(LocalDate.parse("2022-05-05")).build();
        ShowEntity showEntityTwo = ShowEntity.fromDomain(showTwo);
        showEntityTwo.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntityTwo);

        List<Show> fromDb = showRepository.findAllShows();

        assertThat(fromDb).containsOnlyOnce(showEntityOne.toDomain(),showEntityTwo.toDomain());
    }

    @Test
    void whenFindById_thenReturnShow() {
        Category category = Category.builder().name(MUSIC_SHOWS).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        entityManager.persistAndFlush(categoryEntity);

        Show actualShow = Show.builder()
                .name(SHOWS_KISS)
                .description("The band's final concert")
                .image("https://en.wikipedia.org/wiki/End_of_the_Road_World_Tour#/media/File:KISS_EndoftheRoadTourPromo.jpg")
                .duration(120.0)
                .capacity(45000L)
                .onSaleDate(LocalDate.parse("2022-05-18")).build();
        ShowEntity showEntity = ShowEntity.fromDomain(actualShow);
        showEntity.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntity);

        Show fromDb = showRepository.findShowById(showEntity.getId()).get();

        assertThat(fromDb.getName()).isEqualTo(showEntity.getName());
    }

    @Test
    void whenFindByName_thenReturnShow() {
        Category category = Category.builder().name(MUSIC_SHOWS).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        entityManager.persistAndFlush(categoryEntity);

        Show actualShow = Show.builder()
                .name(SHOWS_KISS)
                .description("The band's final concert")
                .image("https://en.wikipedia.org/wiki/End_of_the_Road_World_Tour#/media/File:KISS_EndoftheRoadTourPromo.jpg")
                .duration(120.0)
                .capacity(45000L)
                .onSaleDate(LocalDate.parse("2022-05-18")).build();
        ShowEntity showEntity = ShowEntity.fromDomain(actualShow);
        showEntity.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntity);

        List<Show> fromDb = showRepository.findShowsByName(showEntity.getName());

        assertThat(fromDb).containsOnlyOnce(showEntity.toDomain());
    }

    @Test
    void whenFindByCategory_thenReturnShows() {
        Category category = Category.builder().name(MUSIC_SHOWS).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        entityManager.persistAndFlush(categoryEntity);

        Show showOne = Show.builder()
                .name(SHOWS_KISS)
                .description("The band's final concert")
                .image("https://en.wikipedia.org/wiki/End_of_the_Road_World_Tour#/media/File:KISS_EndoftheRoadTourPromo.jpg")
                .duration(120.0)
                .capacity(45000L)
                .onSaleDate(LocalDate.parse("2022-05-18")).build();
        ShowEntity showEntityOne = ShowEntity.fromDomain(showOne);
        showEntityOne.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntityOne);

        Show showTwo = Show.builder()
                .name(SHOWS_METALLICA)
                .description("WorldWired Tour")
                .image("https://mariskalrock.com/wp-content/uploads/2022/02/metallica-cartel-gira-22-vegas.jpgjpg")
                .duration(120.0)
                .capacity(50000L)
                .onSaleDate(LocalDate.parse("2022-05-05")).build();
        ShowEntity showEntityTwo = ShowEntity.fromDomain(showTwo);
        showEntityTwo.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntityTwo);

        List<Show> fromDb = showRepository.findShowsByCategory(categoryEntity.getId());

        assertThat(fromDb).containsOnlyOnce(showEntityOne.toDomain(),showEntityTwo.toDomain());
    }

    @Test
    void should_create_show() {
        Category category = Category.builder().name(MUSIC_SHOWS).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        entityManager.persistAndFlush(categoryEntity);

        Show showOne = Show.builder()
                .name(SHOWS_KISS)
                .description("The band's final concert")
                .image("https://en.wikipedia.org/wiki/End_of_the_Road_World_Tour#/media/File:KISS_EndoftheRoadTourPromo.jpg")
                .duration(120.0)
                .capacity(45000L)
                .onSaleDate(LocalDate.parse("2022-05-18")).build();
        showOne.setCategory(categoryEntity.toDomain());

        Long fromDb = showRepository.createShow(showOne);

        ShowEntity showEntityOne = entityManager.find(ShowEntity.class, fromDb);
        assertThat(showEntityOne.getName()).isEqualTo(showOne.getName());
    }


    @Test
    void should_update_show() {
        Category category = Category.builder().name(MUSIC_SHOWS).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        entityManager.persistAndFlush(categoryEntity);

        Show showOne = Show.builder()
                .name(SHOWS_KISS)
                .description("The band's final concert")
                .image("https://en.wikipedia.org/wiki/End_of_the_Road_World_Tour#/media/File:KISS_EndoftheRoadTourPromo.jpg")
                .duration(120.0)
                .capacity(45000L)
                .onSaleDate(LocalDate.parse("2022-05-18")).build();
        showOne.setCategory(categoryEntity.toDomain());
        ShowEntity showEntityOne = ShowEntity.fromDomain(showOne);
        showEntityOne.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntityOne);

        Show updateShowOne =showEntityOne.toDomain();

        updateShowOne.setCapacity(32000L);
        showRepository.updateShow(updateShowOne);

        ShowEntity updateShowEntityOne = entityManager.find(ShowEntity.class, updateShowOne.getId());
        assertThat(updateShowEntityOne.getCapacity()).isEqualTo(updateShowOne.getCapacity());
    }

    @Test
    void should_addPerformance_to_show() {
        Category category = Category.builder().name(MUSIC_SHOWS).build();
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        entityManager.persistAndFlush(categoryEntity);

        Show showOne = Show.builder()
                .name(SHOWS_KISS)
                .description("The band's final concert")
                .image("https://en.wikipedia.org/wiki/End_of_the_Road_World_Tour#/media/File:KISS_EndoftheRoadTourPromo.jpg")
                .duration(120.0)
                .capacity(45000L)
                .onSaleDate(LocalDate.parse("2022-05-18")).build();
        showOne.setCategory(categoryEntity.toDomain());
        ShowEntity showEntityOne = ShowEntity.fromDomain(showOne);
        showEntityOne.setCategory(categoryEntity);
        entityManager.persistAndFlush(showEntityOne);

        Show createShowOne =showEntityOne.toDomain();

        Performance performanceOne = Performance.builder()
                .date(LocalDate.parse("2022-05-15"))
                .time(LocalTime.parse("21:30"))
                .streamingURL("http://foo.bar")
                .remainingSeats(300L)
                .status(Status.CREATED)
                .build();

        showRepository.addShowPerformance(createShowOne.getId(),performanceOne);

        ShowEntity updateShowEntityOne = entityManager.find(ShowEntity.class, createShowOne.getId());
        assertThat(updateShowEntityOne.getPerformances()).hasSize(1);
        assertThat(updateShowEntityOne.getPerformances().stream().findFirst().get().getDate()).isEqualTo(performanceOne.getDate());
        assertThat(updateShowEntityOne.getPerformances().stream().findFirst().get().getTime()).isEqualTo(performanceOne.getTime());
        assertThat(updateShowEntityOne.getPerformances().stream().findFirst().get().getStreamingURL()).isEqualTo(performanceOne.getStreamingURL());
        assertThat(updateShowEntityOne.getPerformances().stream().findFirst().get().getRemainingSeats()).isEqualTo(performanceOne.getRemainingSeats());

    }
}
