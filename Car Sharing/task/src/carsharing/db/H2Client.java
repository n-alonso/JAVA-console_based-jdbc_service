package carsharing.db;

import java.sql.*;
import java.util.function.Consumer;

public class H2Client implements DBClient {

    private final Connection conn;

    private static H2Client instance;

    private H2Client(Connection conn) { // Singleton
        this.conn = conn;
    }

    public static H2Client getInstance(String url) {
        if (instance == null) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
                if (conn.isValid(5)) {
                    System.out.println("Connection is valid.");
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            instance = new H2Client(conn);
        }
        return instance;
    }

    public int run(String sql) {
        int output = -1;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            output = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output;
    }

    public void select(String sql, Consumer<ResultSet> handler) {
        try (
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet results = stmt.executeQuery();
        ) {
            handler.accept(results);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            run("TRUNCATE TABLE car;");
            run("TRUNCATE TABLE customer;");
            run("TRUNCATE TABLE company;");
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
