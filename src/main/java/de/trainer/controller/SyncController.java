package de.trainer.controller;

import de.trainer.service.GarminSyncException;
import de.trainer.service.GarminSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
public class SyncController {

    private final GarminSyncService garminSyncService;

    @PostMapping
    public ResponseEntity<String> sync() {
        return ResponseEntity.ok(garminSyncService.sync());
    }

    @ExceptionHandler(GarminSyncException.class)
    public ResponseEntity<String> handleSyncException(GarminSyncException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
    }
}
