package carsharing.dao;

import carsharing.db.DBClient;
import carsharing.db.H2Client;
import carsharing.models.Car;
import carsharing.models.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO implements DAO<Company> {

    private final DBClient client;

    private final String CREATE_COMPANY_TABLE = "CREATE TABLE IF NOT EXISTS company ("
            + "id INT PRIMARY KEY AUTO_INCREMENT,"
            + "name VARCHAR UNIQUE NOT NULL);";

    private final String FIND_ALL_COMPANIES = "SELECT * FROM company;";

    private final String FIND_COMPANY = "SELECT * FROM company WHERE id = %d;";

    private final String INSERT_COMPANY = "INSERT INTO company (name) VALUES ('%s');";

    private final String UPDATE_COMPANY = "UPDATE company SET name = '%s' WHERE id = %d;";

    private final String DELETE_COMPANY = "DELETE FROM company WHERE id = %d;";

    public CompanyDAO(DBClient client) {
        this.client = client;
    }

    @Override
    public List<Company> findAll() {
        List<Company> companies = new ArrayList<>();

        client.select(FIND_ALL_COMPANIES, resultSet -> {
            try {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    companies.add(new Company(id, name));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return companies;
    }

    public Company findById(int id) {
        List<Company> companies = new ArrayList<>();

        client.select(FIND_COMPANY.formatted(id), resultSet -> {
            try {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    companies.add(new Company(id, name));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        if (companies.size() == 0) {
            return null;
        } else {
            return companies.get(0);
        }
    }

    @Override
    public int create() {
        return client.run(CREATE_COMPANY_TABLE);
    }

    @Override
    public int insert(Company company) {
        // INSERT INTO COMPANY (id, name) VALUES (%d, %s);
        return client.run(INSERT_COMPANY.formatted(company.getName()));
    }

    @Override
    public int updateById(int id, Company company) {
        // UPDATE COMPANY SET name = %s WHERE id = %d;
        return client.run(UPDATE_COMPANY.formatted(company.getName(), id));
    }

    @Override
    public int deleteById(int id) {
        // DELETE FROM COMPANY WHERE id = %d;
        return client.run(DELETE_COMPANY.formatted(id));
    }
}
