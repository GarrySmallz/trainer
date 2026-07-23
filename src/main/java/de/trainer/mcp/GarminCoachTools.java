package de.trainer.mcp;

import de.trainer.dto.ActivityOverview;
import de.trainer.dto.WeeklyStats;
import de.trainer.repository.GarminActivityRepository;
import de.trainer.service.GarminSyncService;
import de.trainer.service.SessionContextBuilder;
import de.trainer.service.WeeklySummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GarminCoachTools {

    private final GarminActivityRepository repository;

    private final SessionContextBuilder contextBuilder;

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
            description = "Synchronisiert die Garmin-Datenbank mit den neuesten Aktivitäten von Garmin Connect")
    public String syncGarminData() {
        return garminSyncService.sync();
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
}
