package de.trainer.dto;

import java.util.List;

public record AthleteProfile(
   String name,
   String sport,
   String level,
   String currentGoal,
   java.time.LocalDate goalDate,
   String coachNotes,
   List<String> constraints,
   String preferredLanguage
) {
    public AthleteProfile {
        // prevent manipulation of constraints
        constraints = constraints != null ?
                List.copyOf(constraints) : List.of();
    }
}

