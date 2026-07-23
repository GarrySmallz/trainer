package de.trainer.service;

import de.trainer.dto.ActivitySummary;
import de.trainer.dto.AthleteProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class SessionContextBuilderTest {

    private ActivitySummary minimalActivity(String activityId, String name) {
        return new ActivitySummary(activityId, name, null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null,
                null, null);



    }

    private AthleteProfile minimalAthleteProfile(String name, String sport) {
        return new AthleteProfile(name, sport, null, null,
                null, null, null, null);
    }

    @Test
    @DisplayName("Builds session context without athlete profile")
    void build() {
        SessionContextBuilder sessionContextBuilder = new SessionContextBuilder(Optional.empty());

        String output = sessionContextBuilder.build(minimalActivity("123", "Morning Run"));

        assertThat(output).contains("123").contains("Morning Run");
    }

    @Test
    @DisplayName("Does not include athlete profile when not available")
    void keinAthletProfil() {
        SessionContextBuilder sessionContextBuilder = new SessionContextBuilder(Optional.empty());

        String output = sessionContextBuilder.build(minimalActivity("123", "Morning Run"));

        assertThat(output).doesNotContain("Athlet");
    }

    @Test
    @DisplayName("Includes athlete profile when available")
    void athletenProfilWennVerfuegbar() {
        SessionContextBuilder sessionContextBuilder = new SessionContextBuilder(Optional.of(
                minimalAthleteProfile("John Doe", "Running")));

        String output = sessionContextBuilder.build(minimalActivity("123", "Morning Run"));

        assertThat(output).contains("John Doe");

    }



}
