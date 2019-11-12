package sql;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

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
        String sql = "SELECT COUNT(*) * 100 / (SELECT COUNT(*) FROM survey_results_public) AS Percentage\n" +
                "FROM survey_results_public\n" +
                "WHERE Hobby = ?\n" +
                "GROUP BY Hobby;";
        double codingNoHobby = jdbcTemplate.selectObjectTemplate(sql,
                (rs) -> rs.getDouble("Percentage"), "No");
        assertThat(codingNoHobby).isCloseTo(19.2, Offset.offset(0.1));
        assertThat(100 - codingNoHobby).isCloseTo(80.8, Offset.offset(0.1));
    }

    @Test
    void percentageOfCodingAsHobbyTimeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            String sql = "SELECT COUNT(*) * 100 / (SELECT COUNT(*) FROM survey_results_public) AS Percentage\n" +
                    "FROM survey_results_public\n" +
                    "WHERE Hobby = ?\n" +
                    "GROUP BY Hobby;";
            double codingNoHobby = jdbcTemplate.selectObjectTemplate(sql,
                    (rs) -> rs.getDouble("Percentage"), "No");
        });
    }
}
