package de.trainer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GarminSyncService {

    private final String scriptPath;

    private final String pythonPath;

    private final AtomicReference<SyncStatus> status = new AtomicReference<>(SyncStatus.IDLE);
    private final AtomicReference<String> lastResult = new AtomicReference<>();

    private static final Logger log = LoggerFactory.getLogger(GarminSyncService.class);

    public GarminSyncService(@Value("${garmin.sync.script.path}") String scriptPath,
                             @Value("${garmin.sync.python.path}") String pythonPath) {
        this.scriptPath = scriptPath;
        this.pythonPath = pythonPath;
    }

    public String sync() {

        ProcessBuilder pb = new ProcessBuilder(
                pythonPath, scriptPath, "--all", "--download", "--import", "--analyze", "--latest"
        );
        pb.redirectErrorStream(true);

        int exitCode;
        StringBuilder output = new StringBuilder();
        try {
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);           // live in der Server-Konsole
                    output.append(line).append("\n");  // für später
                }
            }
            exitCode = process.waitFor();
        } catch (IOException e) {
            throw new GarminSyncException("Sync-Prozess konnte nicht gestartet/gelesen werden", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GarminSyncException("Sync wurde unterbrochen", e);
        }

        if (exitCode != 0) {
            throw new GarminSyncException("Sync fehlgeschlagen (Exit " + exitCode + "): " + output);
        }

        return output.toString();
    }

    @Async
    public void syncAsync() {
        status.set(SyncStatus.RUNNING);
        try {
            String output = sync();
            lastResult.set(output);
            status.set(SyncStatus.SUCCESS);
        } catch (GarminSyncException e) {
            lastResult.set(e.getMessage());
            status.set(SyncStatus.FAILURE);
        }
    }

    public SyncStatus getStatus() {
        return status.get();
    }

    public String getLastResult() {
        return lastResult.get();
    }
}
