package de.trainer.service;

import de.trainer.dto.ActivitySummary;
import de.trainer.dto.WeeklyStats;
import de.trainer.repository.GarminActivityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class WeeklySummaryServiceTest {


    private ActivitySummary activityWith(LocalDateTime startTime, Double distance, Double vo2Max) {
        return new ActivitySummary(null, null, null, startTime, null, null,
                distance, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, vo2Max, null, null,
                null, null, null,
                null, null);
    }

    @Test
    void aktivitaetenDerselbenWocheWerdenKorrektAggregiert() {
        GarminActivityRepository mockRepository = Mockito.mock(GarminActivityRepository.class);
        LocalDateTime now = LocalDateTime.now();

        ActivitySummary dieseWoche = activityWith(now.minusDays(1), 10.0, 45.0);
        ActivitySummary dieseWoche2 = activityWith(now.minusDays(2), 10.0, 45.0);

        Mockito.when(mockRepository.findSince(any()))
                .thenReturn(List.of(dieseWoche, dieseWoche2));

        WeeklySummaryService service = new WeeklySummaryService(mockRepository);
        List<WeeklyStats> result = service.weeklySummary();

        assertThat(result).hasSize(1);
        WeeklyStats aktuelleWoche = result.get(0);
        assertThat(aktuelleWoche.activityCount()).isEqualTo(2);
        assertThat(aktuelleWoche.totalDistanceKm()).isEqualTo(20.0);
        assertThat(aktuelleWoche.avgVo2Max()).isEqualTo(45.0);
    }

    @Test
    void aktivitaetenVerschiedenerWochenWerdenGetrennt() {
        GarminActivityRepository mockRepository = Mockito.mock(GarminActivityRepository.class);
        LocalDateTime now = LocalDateTime.now();

        ActivitySummary dieseWoche = activityWith(now.minusDays(1), 10.0, 45.0);
        ActivitySummary letzteWoche = activityWith(now.minusDays(8), 5.0, 40.0);

        Mockito.when(mockRepository.findSince(any()))
                .thenReturn(List.of(dieseWoche, letzteWoche));

        WeeklySummaryService service = new WeeklySummaryService(mockRepository);
        List<WeeklyStats> result = service.weeklySummary();

        assertThat(result).hasSize(2);

        WeeklyStats aktuelleWoche = result.stream()
                .filter(w -> w.weekstart().equals(now.toLocalDate()))
                .findFirst().orElseThrow();
        assertThat(aktuelleWoche.activityCount()).isEqualTo(1);

        WeeklyStats vorherigeWoche = result.stream()
                .filter(w -> w.weekstart().equals(now.toLocalDate().minusWeeks(1)))
                .findFirst().orElseThrow();
        assertThat(vorherigeWoche.activityCount()).isEqualTo(1);
    }
}
