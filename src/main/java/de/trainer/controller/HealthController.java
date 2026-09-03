package de.trainer.controller;

import de.trainer.dto.HealthSummary;
import de.trainer.repository.GarminHealthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final GarminHealthRepository repository;

    @GetMapping
    public List<HealthSummary> list(@RequestParam(defaultValue = "30") int limit) {
        return repository.findRecent(limit);
    }
}
