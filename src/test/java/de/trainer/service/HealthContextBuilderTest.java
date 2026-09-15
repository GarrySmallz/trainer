package de.trainer.service;

import de.trainer.dto.AthleteProfile;
import de.trainer.dto.HealthSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HealthContextBuilderTest {

    private AthleteProfile minimalAthleteProfile(String name, String sport) {
        return new AthleteProfile(name, sport, null, null,
                null, null, null, null);
    }

    private List<HealthSummary> healthSummaries() {
        return List.of(new HealthSummary(LocalDate.of(2024, 6, 1), 70, 60, 120, 80.0,
                98, 36.5, 7.5, 8.0, 70.0,
                "01:00:00", "00:20:00", "00:30:00", "00:10:00", "00:15:00"));
    }

    private List<HealthSummary> healthSummariesWithNulls() {
        return List.of(new HealthSummary(LocalDate.of(2024, 6, 1), null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null));
    }

    @Test
    @DisplayName("Build health context without athlete profile")
    void buildHealthContextWithoutAthleteProfile() {
        HealthContextBuilder builder = new HealthContextBuilder(Optional.empty());
        String output = builder.build(healthSummaries());

        assertThat(output).isNotNull();
    }

    @Test
    @DisplayName("Build health context with null values in health summaries")
    void buildHealthContextWithNullValues() {
        HealthContextBuilder builder = new HealthContextBuilder(Optional.of(minimalAthleteProfile("John Doe", "Running")));
        String output = builder.build(healthSummariesWithNulls());

        assertThat(output).isNotNull().doesNotContain("null");
    }

}
