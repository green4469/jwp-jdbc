package slipp.dao;

import java.util.ArrayList;
import java.util.List;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;

public class UserDao {
    private final JdbcTemplate JDBC_TEMPLATE;

    public UserDao() {
        this.JDBC_TEMPLATE = new JdbcTemplate();
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JDBC_TEMPLATE.updateTemplate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        JDBC_TEMPLATE.updateTemplate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return JDBC_TEMPLATE.selectTemplate(sql, (rs) -> {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email"));
                users.add(user);
            }
            return users;
        });
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return JDBC_TEMPLATE.selectTemplate(sql, (rs) -> {
            List<User> users = new ArrayList<>();
            if (rs.next()) {
                User user = new User(rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email"));
                users.add(user);
            }
            return users;
        }, userId).get(0);
    }
}
