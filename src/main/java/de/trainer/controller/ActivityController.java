package de.trainer.controller;

import de.trainer.dto.ActivitySummary;
import de.trainer.repository.GarminActivityRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final GarminActivityRepository repository;

    public ActivityController(GarminActivityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ActivitySummary> list(@RequestParam(defaultValue = "10") int limit) {
        return repository.findRecent(limit);
    }
}
