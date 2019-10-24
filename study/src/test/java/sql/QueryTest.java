package sql;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

import static org.assertj.core.api.Assertions.assertThat;


public class QueryTest {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ConnectionManager.initialize();
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    void countTotalRecords() {
        String sql = "SELECT COUNT(*) AS num FROM survey_results_public";
        int numOfRecords = jdbcTemplate.selectObjectTemplate(sql, (rs) -> rs.getInt("num"));
        assertThat(numOfRecords).isEqualTo(98855);
    }

    @Test
    void percentOfCodingAsHobby() {
        String sql = "SELECT COUNT(*) AS num FROM survey_results_public";
        int numOfRecords = jdbcTemplate.selectObjectTemplate(sql, (rs) -> rs.getInt("num"));

        sql = "SELECT COUNT(*) AS num FROM survey_results_public WHERE Hobby = ?";
        int numOfCodingAsHobby = jdbcTemplate.selectObjectTemplate(sql, (rs) -> rs.getInt("num"), "Yes");
        double codingAsHobbyPercentage = (double) numOfCodingAsHobby / numOfRecords * 100;
        double codingIsNotHobbyPercentage = 100 - codingAsHobbyPercentage;
        assertThat(codingAsHobbyPercentage).isCloseTo(80.8, Offset.offset(0.1));
        assertThat(codingIsNotHobbyPercentage).isCloseTo(19.2, Offset.offset(0.1));
    }

    @Test
    void percentageOfCodingAsHobbyTimeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            String sql = "SELECT COUNT(*) AS num FROM survey_results_public";
            int numOfRecords = jdbcTemplate.selectObjectTemplate(sql, (rs) -> rs.getInt("num"));
//
//            sql = "SELECT COUNT(*) AS num FROM survey_results_public WHERE Hobby = ?";
//            int numOfCodingAsHobby = jdbcTemplate.selectObjectTemplate(sql, (rs) -> rs.getInt("num"), "Yes");
        });
    }
}
