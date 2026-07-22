package de.trainer.controller;

import de.trainer.dto.ActivitySummary;
import de.trainer.repository.GarminActivityRepository;
import de.trainer.service.SessionContextBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final GarminActivityRepository repository;

    private final SessionContextBuilder contextBuilder;

    public ActivityController(GarminActivityRepository repository, SessionContextBuilder contextBuilder) {
        this.repository = repository;
        this.contextBuilder = contextBuilder;
    }

    @GetMapping
    public List<ActivitySummary> list(@RequestParam(defaultValue = "10") int limit) {
        return repository.findRecent(limit);
    }

    @GetMapping("/latest")
    public ActivitySummary latest() {
        return repository.findLatest();
    }

    @GetMapping("/latest/context")
    public String latestContext() {
        return contextBuilder.build(repository.findLatest());
    }

    @GetMapping("/latest/laps/context")
    public String latestLapsContext() {
        ActivitySummary latestActivity = repository.findLatest();
        return contextBuilder.build(latestActivity, repository.findLaps(latestActivity.activityId()));
    }
}
