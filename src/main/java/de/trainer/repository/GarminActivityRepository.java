package de.trainer.repository;

import de.trainer.dto.ActivitySummary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GarminActivityRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ActivitySummary> rowMapper = (rs, rowNum) -> new ActivitySummary(
            rs.getString("activity_id"),
            rs.getString("name"),
            rs.getTimestamp("start_time").toLocalDateTime(),
            rs.getObject("distance") != null ? rs.getDouble("distance") : null,
            rs.getObject("avg_hr") != null ? rs.getInt("avg_hr") : null,
            rs.getObject("training_effect") != null ? rs.getDouble("training_effect") : null
    );

    public GarminActivityRepository(
            @Qualifier("garminJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<ActivitySummary> findRecent(int limit) {
        String sql = """
            SELECT activity_id, name, start_time, distance, avg_hr, training_effect
            FROM running_activities_view
            ORDER BY start_time DESC
            LIMIT ?
            """;
        return jdbcTemplate.query(sql, rowMapper, limit);
    }
}