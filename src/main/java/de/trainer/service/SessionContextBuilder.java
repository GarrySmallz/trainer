package de.trainer.service;

import de.trainer.dto.ActivitySummary;
import org.springframework.stereotype.Service;

@Service
public class SessionContextBuilder {
    public String build(ActivitySummary a) {
        StringBuilder md = new StringBuilder();
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
}
