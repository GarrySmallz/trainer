package de.trainer.service;

import de.trainer.dto.AthleteProfile;
import de.trainer.dto.HealthSummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static de.trainer.service.SessionContextBuilder.appendAthleteProfile;

@Service
public class HealthContextBuilder {

    private final Optional<AthleteProfile> athleteProfile;

    public HealthContextBuilder(Optional<AthleteProfile> athleteProfile) {
        this.athleteProfile = athleteProfile;
    }


    public String build(List<HealthSummary> days) {
        StringBuilder md = new StringBuilder();
        appendAthleteProfile(md, athleteProfile);
        md.append("# Health-Kontext (Garmin)\n\n");

        if (days == null || days.isEmpty()) {
            md.append("Keine Health-Daten vorhanden.\n");
            return md.toString();
        }

         md.append("| Tag | RHR | Schlaf | Stress | Body Battery Min | Body Battery Max | Schritte |\n");
         md.append("|-----|-----|--------|--------|------------------|------------------|----------|\n");


         for (HealthSummary day : days) {
             md.append("| ").append(day.day())
               .append(" | ").append(cell(day.rhr()))
               .append(" | ").append(cell(day.totalSleep()))
               .append(" | ").append(cell(day.stressAvg()))
                .append(" | ").append(cell(day.bbMin()))
               .append(" | ").append(cell(day.bbMax()))
               .append(" | ").append(cell(day.steps()))
               .append(" |\n");
        }

        md.append("\n## Hinweise\n");
        md.append("- Interpretiere nur die genannten Werte\n");
        md.append("- Kein medizinischer Rat\n");
        md.append("- Bei fehlenden Werten nicht spekulieren\n");

        return md.toString();
    }
    private String cell(Object value) {
        return value != null ? value.toString().split("\\.")[0] : "–"; }
}