package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public <T> List<T> selectTemplate(String sql, RowMapper2<T> rowMapper, Object... params) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), params);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> result = new ArrayList<>();
            while (rs.next()) {
              result.add(rowMapper.mapRow(rs));
            }
            return result;
        } catch (Exception e) {
            throw new SelectQueryFailException();
        }
    }

    public <T> List<T> selectTemplate(String sql, PreparedStatementSetter pstmtSetter, RowMapper2<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), pstmtSetter);
             ResultSet rs = pstmt.executeQuery()) {
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
            return result;
        } catch (Exception e) {
            throw new SelectQueryFailException();
        }
    }

    public void updateTemplate(String sql, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new UpdateQueryFailException();
        }
    }

    public void updateTemplate(String sql, Object... params) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), params)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new UpdateQueryFailException();
        }
    }

    private PreparedStatement setValues(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 1; i <= params.length; i++) {
            pstmt.setObject(i, params[i - 1]);
        }
        return pstmt;
    }

    private PreparedStatement setValues(PreparedStatement pstmt, PreparedStatementSetter pstmtSetter) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return pstmt;
    }
}
