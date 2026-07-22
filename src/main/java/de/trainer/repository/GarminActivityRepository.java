package de.trainer.repository;

import de.trainer.dto.ActivitySummary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class GarminActivityRepository {

    private static final String SELECT_COLUMNS = """
            activity_id, name, sub_sport,
            start_time, stop_time, elapsed_time,
            distance, steps,
            avg_pace, avg_moving_pace, max_pace,
            avg_steps_per_min, max_steps_per_min,
            avg_hr, max_hr, calories,
            avg_temperature, avg_speed, max_speed, avg_stance_time_percent,
            vo2_max, training_effect, anaerobic_training_effect,
            heart_rate_zone_one_time, heart_rate_zone_two_time,
            heart_rate_zone_three_time, heart_rate_zone_four_time,
            heart_rate_zone_five_time
            """;

    private final JdbcTemplate jdbcTemplate;


    // macht aus jeder Zeile in DB ein Javaobjekt
    private final RowMapper<ActivitySummary> rowMapper = (rs, rowNum) -> new ActivitySummary(
            rs.getString("activity_id"),
            rs.getString("name"),
            rs.getString("sub_sport"),
            toLocalDateTime(rs, "start_time"),
            toLocalDateTime(rs, "stop_time"),
            rs.getString("elapsed_time"),
            getDouble(rs, "distance"),
            getInteger(rs, "steps"),
            rs.getString("avg_pace"),
            rs.getString("avg_moving_pace"),
            rs.getString("max_pace"),
            getInteger(rs, "avg_steps_per_min"),
            getInteger(rs, "max_steps_per_min"),
            getInteger(rs, "avg_hr"),
            getInteger(rs, "max_hr"),
            getInteger(rs, "calories"),
            getDouble(rs, "avg_temperature"),
            getDouble(rs, "avg_speed"),
            getDouble(rs, "max_speed"),
            getDouble(rs, "vo2_max"),
            getDouble(rs, "training_effect"),
            getDouble(rs, "anaerobic_training_effect"),
            rs.getString("heart_rate_zone_one_time"),
            rs.getString("heart_rate_zone_two_time"),
            rs.getString("heart_rate_zone_three_time"),
            rs.getString("heart_rate_zone_four_time"),
            rs.getString("heart_rate_zone_five_time")
    );

    public GarminActivityRepository(
            @Qualifier("garminJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ActivitySummary> findRecent(int limit) {
        String sql = """
                SELECT %s
                FROM running_activities_view
                ORDER BY start_time DESC
                LIMIT ?
                """.formatted(SELECT_COLUMNS);
        return jdbcTemplate.query(sql, rowMapper, limit);
    }

    public ActivitySummary findLatest() {
        String sql = """
                SELECT %s
                FROM running_activities_view
                ORDER BY start_time DESC
                LIMIT 1
                """.formatted(SELECT_COLUMNS);
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    private static java.time.LocalDateTime toLocalDateTime(ResultSet rs, String column) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(column);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private static Double getDouble(ResultSet rs, String column) throws SQLException {
        Object value = rs.getObject(column);
        return value != null ? rs.getDouble(column) : null;
    }

    private static Integer getInteger(ResultSet rs, String column) throws SQLException {
        Object value = rs.getObject(column);
        return value != null ? rs.getInt(column) : null;
    }
}
