package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class ConnectionManager {
    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    public static void initialize(String _driver, String url, String userName, String password) {
        DRIVER = _driver;
        URL = url;
        USERNAME = userName;
        PASSWORD = password;
    }

    public static void initialize() {
        InputStream inputStream = (ConnectionManager.class).getClassLoader()
                .getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new ReadPropertiesFailException();
        }
        DRIVER = properties.getProperty("jdbc.driverClass");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.username");
        PASSWORD = properties.getProperty("jdbc.password");
    }

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DRIVER);
        ds.setUrl(URL);
        ds.setUsername(USERNAME);
        ds.setPassword(PASSWORD);
        return ds;
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
