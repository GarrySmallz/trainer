package de.trainer.dto;

import java.time.LocalDateTime;

public record ActivitySummary(
        String activityId,
        String name,
        LocalDateTime startTime,
        Double distance,
        Integer avgHr,
        Double trainingEffect
) {}