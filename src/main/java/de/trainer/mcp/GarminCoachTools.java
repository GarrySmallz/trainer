package de.trainer.mcp;

import de.trainer.dto.ActivityOverview;
import de.trainer.dto.WeeklyStats;
import de.trainer.repository.GarminActivityRepository;
import de.trainer.repository.GarminHealthRepository;
import de.trainer.service.GarminSyncService;
import de.trainer.service.HealthContextBuilder;
import de.trainer.service.SessionContextBuilder;
import de.trainer.service.WeeklySummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

import static de.trainer.service.SyncStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GarminCoachTools {

    private final GarminActivityRepository repository;

    private final SessionContextBuilder contextBuilder;

    private final GarminHealthRepository healthRepository;
    private final HealthContextBuilder healthContextBuilder;

    private final GarminSyncService garminSyncService;
    private final WeeklySummaryService weeklySummaryService;

    @McpTool(name = "get_latest_activity_context",
                description = "Liefert den vollständigen sportwissenschaftlichen Kontext " +
                        "(Athlet + letzte Aktivität) der neuesten Garmin-Session")
    public String getLatestActivityContext() {
        return contextBuilder.build(repository.findLatest());
    }

    @McpTool(name = "list_recent_activities", description = "Listet die letzten N Aktivitäten mit Kurzübersicht " +
            "(Datum, Name, Distanz, Pace, Training Effect)")
    public List<ActivityOverview> listRecentActivities(
            @McpToolParam(description = "Anzahl der zurückzugebenden Aktivitäten", required = false) Integer limit) {
        int actualLimit = limit != null ? limit : 10;
        return repository
                .findRecent(actualLimit)
                .stream()
                .map(ActivityOverview::from)
                .toList();
    }

    @McpTool(name = "sync_garmin_data",
            description = "Startet den Garmin-Sync im Hintergrund und kehrt sofort zurück")
    public String syncGarminData() {
        if (garminSyncService.getStatus().equals(RUNNING)) {
            return "Sync läuft bereits";
        }
        garminSyncService.syncAsync();
        return "Sync gestartet: Nutze get_sync_status, um den Fortschritt zu überprüfen.";
    }

    @McpTool(name = "get_sync_status", description = "Prüft den Status des zuletzt gestarteten Garmin-Syncs")
    public String getSyncStatus() {
        return switch (garminSyncService.getStatus()) {
            case IDLE -> "Noch kein Sync gestartet.";
            case RUNNING -> "Sync läuft noch.";
            case SUCCESS -> "Sync erfolgreich abgeschlossen." + garminSyncService.getLastResult();
            case FAILURE -> "Sync fehlgeschlagen: " + garminSyncService.getLastResult();
            default -> throw new IllegalStateException("Unexpected value: " + garminSyncService.getStatus());
        };
    }

    @McpTool(name = "get_activity_context",
            description = "Liefert den vollständigen sportwissenschaftlichen Kontext (Athlet + Aktivität) für eine bestimmte Aktivität")
    public String getActivityContext(
            @McpToolParam(description = "Die Garmin activity_id der gewünschten Aktivität", required = true) String activityId) {
        return contextBuilder.build(repository.findById(activityId));
    }

    @McpTool(name = "get_weekly_summary",
    description = "Liefert eine wöchentliche Zusammenfassung der letzten 8 Wochen (Anzahl Aktivitäten, km, Ø VO2max pro Woche)")
    public List<WeeklyStats> getWeeklySummary() {
        return weeklySummaryService.weeklySummary();

    }

    @McpTool(name = "get_health_context", description = "Liefert Erholungs- und Gesundheitsdaten (Ruhepuls, Schlaf, Stress, Body Battery) " +
            "der letzten N Tage als Kontext. Nutzen bei Fragen zu Erholung, Schlafqualität, " +
            "Stress-Level oder Übertraining – nicht für einzelne Trainingsaktivitäten.")
    public String getHealthContext(
            @McpToolParam(description = "Anzahl der Tage, für die Health-Daten zurückgegeben werden sollen", required = false) Integer limit) {
        int actualLimit = limit != null ? limit : 14;
        return healthContextBuilder.build(healthRepository.findRecent(actualLimit));
    }


}
