package de.trainer.dto;

import java.time.LocalDateTime;

/**
 * Reiche Session-Daten aus {@code running_activities_view} für Co-Trainer-Kontext.
 * Zeit-/Pace-Felder als String (SQLite TIME), damit JDBC sie zuverlässig liest.
 */
public record ActivitySummary(
        String activityId,
        String name,
        String subSport,
        LocalDateTime startTime,
        LocalDateTime stopTime,
        String elapsedTime,
        Double distance,
        Integer steps,
        String avgPace,
        String avgMovingPace,
        String maxPace,
        Integer avgStepsPerMin,
        Integer maxStepsPerMin,
        Integer avgHr,
        Integer maxHr,
        Integer calories,
        Double avgTemperature,
        Double avgSpeed,
        Double maxSpeed,
        Double vo2Max,
        Double trainingEffect,
        Double anaerobicTrainingEffect,
        String heartRateZoneOneTime,
        String heartRateZoneTwoTime,
        String heartRateZoneThreeTime,
        String heartRateZoneFourTime,
        String heartRateZoneFiveTime
) {}
