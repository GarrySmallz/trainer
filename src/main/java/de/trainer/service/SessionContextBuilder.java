package de.trainer.service;

import de.trainer.dto.ActivitySummary;
import de.trainer.dto.AthleteProfile;
import de.trainer.dto.LapSummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionContextBuilder {

    private final Optional<AthleteProfile> athleteProfile;

    public SessionContextBuilder(Optional<AthleteProfile> athleteProfile) {
        this.athleteProfile = athleteProfile;
    }

    public String build(ActivitySummary a) {
        StringBuilder md = new StringBuilder();
        md.append("## Session-Kontext (Athlete)\n\n");
        athleteProfile.ifPresent(profile -> {
            line(md, "Name", profile.name());
            line(md, "Sport", profile.sport());
            line(md, "Level", profile.level());
            line(md, "Aktuelles Ziel", profile.currentGoal());
            line(md, "Ziel Datum", profile.goalDate());
            line(md, "Coach Notes", profile.coachNotes());
            line(md, "Einschränkungen", profile.constraints());
            line(md, "Bevorzugte Sprache", profile.preferredLanguage());
        });
        md.append("# Session-Kontext (Garmin)\n\n");
        md.append("## Meta\n");
        line(md, "Activity-ID", a.activityId());
        line(md, "Name", a.name());
        line(md, "Sub-Sport", a.subSport());
        line(md, "Start", a.startTime());
        line(md, "Ende", a.stopTime());
        line(md, "Dauer", a.elapsedTime());
        md.append("\n## Leistung\n");
        line(md, "Distanz (km)", a.distance());
        line(md, "Schritte", a.steps());
        line(md, "Ø Pace", a.avgPace());
        line(md, "Moving Pace", a.avgMovingPace());
        line(md, "Max Pace", a.maxPace());
        line(md, "Ø Speed", a.avgSpeed());
        line(md, "Max Speed", a.maxSpeed());
        md.append("\n## Herzfrequenz\n");
        line(md, "Ø HF (bpm)", a.avgHr());
        line(md, "Max HF (bpm)", a.maxHr());
        line(md, "Zeit Z1", a.heartRateZoneOneTime());
        line(md, "Zeit Z2", a.heartRateZoneTwoTime());
        line(md, "Zeit Z3", a.heartRateZoneThreeTime());
        line(md, "Zeit Z4", a.heartRateZoneFourTime());
        line(md, "Zeit Z5", a.heartRateZoneFiveTime());
        md.append("\n## Belastung & Fitness\n");
        line(md, "Training Effect (aerob)", a.trainingEffect());
        line(md, "Training Effect (anaerob)", a.anaerobicTrainingEffect());
        line(md, "VO2max", a.vo2Max());
        line(md, "Kalorien", a.calories());
        line(md, "Temperatur (°C)", a.avgTemperature());
        md.append("\n## Hinweise\n");
        md.append("- Interpretiere nur die genannten Werte\n");
        md.append("- Antworte aus sportwissenschaftlicher Sicht\n");
        md.append("- Keine Trainingspläne vorschlagen\n");
        md.append("- Bei fehlenden Werten nicht spekulieren\n");
        return md.toString();
    }

    private void line(StringBuilder md, String label, Object value) {
        if (value == null) {
            return; // null-Felder weglassen
        }
        md.append("- ").append(label).append(": ").append(value).append("\n");
    }

    public String build(ActivitySummary a, List<LapSummary> laps) {
        StringBuilder md = new StringBuilder();
        md.append(build(a)); // Basisinfos aus ActivitySummary
        md.append("\n## Laps / Abschnitte\n");
        if (laps == null || laps.isEmpty()) {
            md.append("- Keine Lap-Daten vorhanden.\n");
        } else {
            md.append("- Hinweis: Distanz/Tempo pro Lap können in GarminDB fehlen (Issue #317).\n");
            for (LapSummary lap : laps) {
                md.append("- Lap ").append(lap.lapNumber()).append(":");
                appendIfPresent(md, "Distanz (km)", lap.distance());
                appendIfPresent(md, "Ø Speed", lap.avgSpeed());
                appendIfPresent(md, "Dauer", lap.elapsedTime());
                appendIfPresent(md, "Ø HF", lap.avgHr());
                appendIfPresent(md, "Max HF", lap.maxHr());
                appendIfPresent(md, "Z1", lap.heartRateZoneOneTime());
                appendIfPresent(md, "Z2", lap.heartRateZoneTwoTime());
                appendIfPresent(md, "Z3", lap.heartRateZoneThreeTime());
                appendIfPresent(md, "Z4", lap.heartRateZoneFourTime());
                appendIfPresent(md, "Z5", lap.heartRateZoneFiveTime());
                md.append("\n");
            }
        }
        // ... Hinweise ...
        return md.toString();
    }

    private void appendIfPresent(StringBuilder md, String label, Object value) {
        if (value == null) {
            return;
        }
        // optionale Filter: "00:00:00" weglassen
        if (value instanceof String s && (s.isBlank() || s.equals("00:00:00"))) {
            return;
        }
        md.append(" ").append(label).append("=").append(value).append(";");
    }
}
