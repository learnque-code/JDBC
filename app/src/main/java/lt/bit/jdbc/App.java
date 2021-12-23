package lt.bit.jdbc;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.Properties;

public class App {
    private final static String USERNAME = "root";
    private final static String PASSWORD = "admin";
    private final static String DATABASE = "lessons";
    private final static String HOST = "127.0.0.1";
    private final static String PORT = "3306";

    public static String getGreeting() {
        return "Hello to JDBC application!";
    }

    public static Connection getConnectionFromDriverManager() throws SQLException {
        Connection connection = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", USERNAME);
        connectionProps.put("password", PASSWORD);

        connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/", HOST, PORT), connectionProps);
        connection.setCatalog(DATABASE);

        return connection;
    }

    public static Connection getConnectionFromDataSource() throws SQLException {
        Connection connection;
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(String.format("jdbc:mysql://%s:%s/", HOST, PORT));
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        connection = dataSource.getConnection();
        connection.setCatalog(DATABASE);

        return connection;
    }

    public static void executeQueryAndPrint(Connection conn, String SQL) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.printf("Order id: %s%n", rs.getString("orderid"));
            }
        }
    }

    public static <T> void executeQueryAndPrint(Connection conn, String SQL, T parameter) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
           if (parameter instanceof Integer) {
              ps.setInt(1, (Integer) parameter);
           } else if (parameter instanceof String) {
               ps.setString(1, (String) parameter);
           } else if (parameter instanceof Date) {
               ps.setDate(1, (Date) parameter);
           } else {
               throw new SQLException("Wrong parameter type passed");
           }

            // for Java 17
//            if (parameter instanceof Integer intParam) {
//                ps.setInt(1, intParam);
//            } else if (parameter instanceof String stringParam ) {
//                ps.setString(1, stringParam);
//            } else if (parameter instanceof Date dateParam) {
//                ps.setDate(1, dateParam);
//            } else {
//                throw new SQLException("Wrong parameter type passed");
//            }

            // Or

            // switch(parameter) {
            //     case Integer intParam -> ps.setInt(1, intParam);
            //     case String stringParam -> ps.setString(1, stringParam);
            //     case Date dateParam -> ps.setDate(1, dateParam);
            //     default -> throw new SQLException("Wrong parameter type passed");
            // }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.printf("Order id: %s%n", rs.getString("orderid"));
            }
        }
    }

    public static void executeQueryWithDriverManager(String SQL) {
        try (Connection conn = getConnectionFromDriverManager()) {
            executeQueryAndPrint(conn, SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeQueryWithDataSource(String SQL) {
        try (Connection conn = getConnectionFromDataSource()) {
            executeQueryAndPrint(conn, SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T> void executeQueryWithDataSource(String SQL, T parameter) {
        try (Connection conn = getConnectionFromDataSource()) {
            executeQueryAndPrint(conn, SQL, parameter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(getGreeting());
//        executeQueryWithDriverManager("SELECT * FROM Orders");
//        executeQueryWithDataSource("SELECT * FROM Orders");
        executeQueryWithDataSource("SELECT * FROM Orders WHERE orderid = ?", 11050);
    }
}
