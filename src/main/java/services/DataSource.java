package services;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DataSource {
    private static final Logger logger = Logger.getLogger(DataSource.class.getName());

    private static Config config = ConfigFactory.load().resolve();
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            String jdbcUrl = String.format("jdbc:mysql://%s/%s", config.getString("db.host"), config.getString("db.db"));
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setJdbcUrl(jdbcUrl);
            cpds.setPassword(config.getString("db.password"));
            cpds.setUser(config.getString("db.user"));
        } catch (PropertyVetoException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private DataSource(){}

    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
}
