package hospital.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conn implements AutoCloseable {

    Connection connection;
    Statement statement;

    public conn(){
        try {
            // Ensure MySQL driver is loaded (useful in some environments)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException driverIgnored) {
                // If the driver is not present, JDBC 4+ may still auto-load via SPI
            }

            String url = System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/hospital_management_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            );
            String user = System.getenv().getOrDefault("DB_USER", "root");
            String pass = System.getenv().getOrDefault("DB_PASSWORD", "root");

            connection = DriverManager.getConnection(url, user, pass);
            statement = connection.createStatement();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignored) {}
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {}
        }
    }
}

