package de.trainer.dto;

import java.time.LocalDate;

public record WeeklyStats(
        LocalDate weekstart,
        int activityCount,
        double totalDistanceKm,
        Double avgVo2Max
) {
}
