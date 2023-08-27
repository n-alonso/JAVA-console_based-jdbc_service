package carsharing.dao;

import carsharing.db.DBClient;
import carsharing.db.H2Client;
import carsharing.models.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO implements DAO<Car> {

    private final DBClient client;

    private final String CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS car ("
            + "id INT PRIMARY KEY AUTO_INCREMENT,"
            + "name VARCHAR UNIQUE NOT NULL,"
            + "company_id INT NOT NULL,"
            + "FOREIGN KEY (company_id) REFERENCES company(id));";

    private final String FIND_ALL_CARS = "SELECT * FROM car;";

    private final String FIND_CAR = "SELECT * FROM car WHERE id = %d;";

    private final String FIND_CAR_BY_COMPANY = "SELECT * FROM car WHERE company_id = %d;";

    private final String INSERT_CAR = "INSERT INTO car (name, company_id) VALUES ('%s', %d);";

    private final String UPDATE_CAR = "UPDATE car SET name = '%s',"
            + "company_id = %d"
            + "WHERE id = %d;";

    private final String DELETE_CAR = "DELETE FROM car WHERE id = %d;";

    public CarDAO(DBClient client) {
        this.client = client;
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();

        client.select(FIND_ALL_CARS, resultSet -> {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int company_id = resultSet.getInt("company_id");
                    cars.add(new Car(id, name, company_id));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return cars;
    }

    public Car findById(int id) {
        List<Car> cars = new ArrayList<>();

        client.select(FIND_CAR.formatted(id), resultSet -> {
            try {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int company_id = resultSet.getInt("company_id");
                    cars.add(new Car(id, name, company_id));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        if (cars.size() == 0) {
            return null;
        } else {
            return cars.get(0);
        }
    }

    public List<Car> findByCompany_id(int company_id) {
        List<Car> cars = new ArrayList<>();

        client.select(FIND_CAR_BY_COMPANY.formatted(company_id), resultSet -> {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    cars.add(new Car(id, name, company_id));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return cars;
    }

    @Override
    public int create() {
        return client.run(CREATE_CAR_TABLE);
    }

    @Override
    public int insert(Car car) {
        // INSERT INTO car (name, company_id) VALUES ('%s', %d);
        return client.run(INSERT_CAR.formatted(car.getName(), car.getCompany_id()));
    }

    @Override
    public int updateById(int id, Car car) {
        // UPDATE car SET name = '%s', company_id = %d WHERE id = %d;
        return client.run(UPDATE_CAR.formatted(car.getName(), car.getCompany_id(), id));
    }

    @Override
    public int deleteById(int id) {
        // DELETE FROM car WHERE id = %d;
        return client.run(DELETE_CAR.formatted(id));
    }
}
