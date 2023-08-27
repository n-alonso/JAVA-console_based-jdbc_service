package carsharing.db;

import java.lang.reflect.InaccessibleObjectException;
import java.sql.ResultSet;
import java.util.function.Consumer;

public interface DBClient {

    static DBClient getInstance(String url) {
        throw new UnsupportedOperationException("This static method should be overridden by concrete classes");
    };
    int run(String query);
    void select(String query, Consumer<ResultSet> handler);
    void disconnect();
}
