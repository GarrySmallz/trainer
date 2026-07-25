package de.trainer.dto;

import java.time.LocalDate;

public record HealthSummary(
        LocalDate day,
        Integer rhr,
        Integer stressAvg,
        Integer steps,
        Double distance,
        Integer caloriesTotal,
        Double bbCharged,
        Double bbMin,
        Double bbMax,
        Double rrWakingAvg,
        String totalSleep,
        String deepSleep,
        String lightSleep,
        String remSleep,
        String awake,
        Double weight
) {
}
