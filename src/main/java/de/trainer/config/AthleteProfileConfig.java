package de.trainer.config;

import de.trainer.dto.AthleteProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Optional;

@Configuration
public class AthleteProfileConfig {

    @Bean
    public Optional<AthleteProfile> athleteProfile(
            ObjectMapper objectMapper) {
        try (InputStream in =
                getClass().getResourceAsStream("/athlete-profile.json")) {
            if (in == null) {
                return Optional.empty();
    }
            return Optional.of(objectMapper.readValue(in, AthleteProfile.class));
        } catch (Exception e) {
            throw new RuntimeException("Error reading athlete profile", e);
        }
    }
}
