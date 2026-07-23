package de.trainer.dto;


import java.time.LocalDateTime;

public record ActivityOverview(
        String activityId,
        LocalDateTime startTime,
        String name,
        Double distance,
        String avgPace,
        Double trainingEffect
) {
    public static ActivityOverview from(ActivitySummary a) {
        return new ActivityOverview(
                a.activityId(),
                a.startTime(),
                a.name(),
                a.distance(),
                a.avgPace(),
                a.trainingEffect()
        );
    }

}

