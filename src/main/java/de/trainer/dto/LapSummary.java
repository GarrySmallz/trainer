package de.trainer.dto;

public record LapSummary(
        Integer lapNumber,
        Double distance,          // oft null (GarminDB #317, warte auf fix)
        Double avgSpeed,          // oft null
        String elapsedTime,       // oft 00:00:00 / null
        Integer avgHr,
        Integer maxHr,
        String heartRateZoneOneTime,
        String heartRateZoneTwoTime,
        String heartRateZoneThreeTime,
        String heartRateZoneFourTime,
        String heartRateZoneFiveTime

) {}
