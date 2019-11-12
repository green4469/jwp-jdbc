package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static String driver;
    private static String url;
    private static String userName;
    private static String password;

    public static void initialize(String _driver, String _url, String _userName, String _password) {
        driver = _driver;
        url = _url;
        userName = _userName;
        password = _password;
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
        driver = properties.getProperty("jdbc.driverClass");
        url = properties.getProperty("jdbc.url");
        userName = properties.getProperty("jdbc.username");
        password = properties.getProperty("jdbc.password");
    }

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);

        try {
            ds.getConnection().close();  // initialize dbcp of basic data source.
        } catch (SQLException e) {
            throw new DataSourceFailException();
        }

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
