package de.trainer.repository;

import de.trainer.dto.HealthSummary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import static de.trainer.repository.GarminActivityRepository.getDouble;
import static de.trainer.repository.GarminActivityRepository.getInteger;


@Repository
public class GarminHealthRepository {

    private static final String SELECT_COLUMNS = """
            day,rhr,stress_avg,steps,distance,
            calories_total, bb_charged, bb_min, bb_max,
            rr_waking_avg, total_sleep, deep_sleep, light_sleep,
            rem_sleep, awake, weight
            """;

    private final JdbcTemplate jdbcTemplate;

    public GarminHealthRepository(
            @Qualifier("garminJdbcHealthTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<HealthSummary> rowMapper = (rs, rowNum) -> new HealthSummary(
                rs.getDate("day").toLocalDate(),
                getInteger(rs, "rhr"),
                getInteger(rs, "stress_avg"),
                getInteger(rs, "steps"),
                getDouble(rs, "distance"),
                getInteger(rs, "calories_total"),
                getDouble(rs, "bb_charged"),
                getDouble(rs, "bb_min"),
                getDouble(rs, "bb_max"),
                getDouble(rs, "rr_waking_avg"),
                rs.getString("total_sleep"),
                rs.getString("deep_sleep"),
                rs.getString("light_sleep"),
                rs.getString("rem_sleep"),
                rs.getString("awake"),
                getDouble(rs, "weight")
    );

}
