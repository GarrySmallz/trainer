package de.trainer.config;

import de.trainer.dto.AthleteProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;

class AthleteProfileConfigTest {



    @Test
    @DisplayName("Test parsing without Athlete Profile")
    void testAthleteProfileConfig()
    {
        AthleteProfileConfig athleteProfileConfig = new AthleteProfileConfig();

        ObjectMapper objectMapper = new ObjectMapper();

        athleteProfileConfig.parseProfile(null, objectMapper);

        assertThat(athleteProfileConfig.parseProfile(null, objectMapper)).isEmpty();
    }

    @Test
    @DisplayName("Test parsing with Athlete Profile")
    void parseProfile()
    {
        AthleteProfileConfig athleteProfileConfig = new AthleteProfileConfig();
        ObjectMapper objectMapper = new ObjectMapper();

        AthleteProfile athleteProfile = new AthleteProfile("Joe", "running",
                null, null, null, null, null, null);

        byte[] json = objectMapper.writeValueAsBytes(athleteProfile);
        InputStream in = new ByteArrayInputStream(json);


        Optional<AthleteProfile> result = athleteProfileConfig.parseProfile(in, objectMapper);

        assertThat(result).contains(athleteProfile);
    }

    @Test
    @DisplayName("Test parsing with invalid JSON")
    void parseProfileWithInvalidJson()
    {
        AthleteProfileConfig athleteProfileConfig = new AthleteProfileConfig();
        ObjectMapper objectMapper = new ObjectMapper();

        String invalidJson = "{ das ist kein JSON";
        InputStream in = new ByteArrayInputStream(invalidJson.getBytes());

        assertThatRuntimeException().isThrownBy(() -> athleteProfileConfig.parseProfile(in, objectMapper));
    }
}
