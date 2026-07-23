package de.trainer.service;

import de.trainer.dto.ActivitySummary;
import de.trainer.dto.WeeklyStats;
import de.trainer.repository.GarminActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeeklySummaryService {

    private final GarminActivityRepository garminActivityRepository;



    public List<WeeklyStats> weeklySummary() {
        LocalDateTime now = LocalDateTime.now();
        List<ActivitySummary> activities = garminActivityRepository.findSince(now.minusWeeks(8));

        Map<Long, List<ActivitySummary>> activitiesByWeek = activities.stream()
                .collect(Collectors.groupingBy(a -> ChronoUnit.DAYS.between(a.startTime(), now) / 7));

        return activitiesByWeek.entrySet().stream()
                .map(entry -> {
                    List<ActivitySummary> weekActivities = entry.getValue();

                    double totalDistance = weekActivities.stream()
                            .mapToDouble(a -> a.distance() != null ? a.distance() : 0.0).sum();

                    OptionalDouble avgVo2MaxOpt = weekActivities.stream()
                            .map(ActivitySummary::vo2Max)
                            .filter(Objects::nonNull)
                            .mapToDouble(Double::doubleValue)
                            .average();

                    Double avgVo2Max = avgVo2MaxOpt.isPresent() ? avgVo2MaxOpt.getAsDouble() : null;

                    LocalDate weekStart = now.toLocalDate().minusWeeks(entry.getKey());
                    return new WeeklyStats(weekStart, weekActivities.size(), totalDistance, avgVo2Max);
                }).sorted(Comparator.comparing(WeeklyStats::weekstart)).toList();
    }
}
