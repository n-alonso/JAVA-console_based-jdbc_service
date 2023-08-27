package carsharing.dao;

import carsharing.db.DBClient;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements DAO<Customer> {

    private final DBClient client;

    private final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS customer ("
            + "id INT PRIMARY KEY AUTO_INCREMENT,"
            + "name VARCHAR UNIQUE NOT NULL,"
            + "rented_car_id INT,"
            + "FOREIGN KEY (rented_car_id) REFERENCES car(id));";

    private final String FIND_ALL_CUSTOMERS = "SELECT * FROM customer";

    private final String FIND_CUSTOMER = "SELECT * FROM customer WHERE id = %d;";

    private final String INSERT_CUSTOMER = "INSERT INTO customer (name, rented_car_id) VALUES ('%s', NULL);";

    private final String UPDATE_CUSTOMER_ADD_CAR = "UPDATE customer SET name = '%s',"
            + "rented_car_id = %d WHERE id = %d;";

    private final String UPDATE_CUSTOMER_REMOVE_CAR = "UPDATE customer SET name = '%s',"
            + "rented_car_id = NULL WHERE id = %d;";

    private final String DELETE_CUSTOMER = "DELETE FROM customer WHERE id = %d;";

    public CustomerDAO(DBClient client) {
        this.client = client;
    }


    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();

        client.select(FIND_ALL_CUSTOMERS, resultSet -> {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Integer rented_car_id = resultSet.getInt("rented_car_id");
                    if (resultSet.wasNull()) {
                        rented_car_id = null;
                    }
                    customers.add(new Customer(id, name, rented_car_id));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return customers;
    }

    public Customer findById(int id) {
        List<Customer> customers = new ArrayList<>();

        client.select(FIND_CUSTOMER.formatted(id + 1), resultSet -> {
            try {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    Integer rented_car_id = resultSet.getInt("rented_car_id");
                    if (resultSet.wasNull()) {
                        rented_car_id = null;
                    }
                    customers.add(new Customer(id, name, rented_car_id));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        if (customers.size() == 0) {
            return null;
        } else {
            return customers.get(0);
        }
    }

    @Override
    public int create() {
        return client.run(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public int insert(Customer customer) {
        // INSERT INTO customer (name, rented_car_id) VALUES ('%s', NULL);
        return client.run(INSERT_CUSTOMER.formatted(customer.getName()));
    }

    @Override
    public int updateById(int id, Customer customer) {
        // UPDATE customer SET name = '%s', rented_car_id = %d WHERE id = %d;
        return client.run(UPDATE_CUSTOMER_ADD_CAR.formatted(customer.getName(), customer.getRented_car_id(), id));
    }

    public int updateByIdRemoveCar(int id, Customer customer) {
        // UPDATE customer SET name = '%s', rented_car_id = NULL WHERE id = %d;
        return client.run(UPDATE_CUSTOMER_REMOVE_CAR.formatted(customer.getName(), id));
    }

    @Override
    public int deleteById(int id) {
        // DELETE FROM car WHERE id = %d;
        return client.run(DELETE_CUSTOMER.formatted(id));
    }
}
