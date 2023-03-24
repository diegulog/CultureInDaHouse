package edu.uoc.epcsd.showcatalog.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.assertThat;

public class ShowUnitTest {
    private static final String SHOWS_KISS = "Kiss - The final tour";

    @Test
    void should_start_with_created_status() {
        Show actualShow = Show.builder().name(SHOWS_KISS).build();
        assertThat(actualShow.getStatus()).isEqualTo(Status.CREATED);
    }
    @Test
    void should_change_status_when_canceled() {
        Show actualShow = Show.builder().name(SHOWS_KISS).build();
        actualShow.cancel();
        assertThat(actualShow.getStatus()).isEqualTo(Status.CANCELLED);
    }
    @Test
    void should_change_the_status_of_performances_when_the_show_is_cancelled() {
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

        Show actualShow = Show.builder()
                .name(SHOWS_KISS)
                .performances(performances)
                .build();

        actualShow.cancel();

        assertThat(actualShow.getPerformances().iterator().next().getStatus()).isEqualTo(Status.CANCELLED);
    }
    @Test
    void should_generate_exception_when_canceling_a_canceled_event() {
        Show actualShow = Show.builder().name(SHOWS_KISS).build();
        actualShow.cancel();
        try{
            actualShow.cancel();
        } catch (RuntimeException e){
            assertThat(e.getMessage()).isEqualTo("Can't cancel an already cancelled show");
            assertThat(e).isInstanceOf(DomainException.class);
        }
    }
}