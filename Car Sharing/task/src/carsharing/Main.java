package carsharing;

import carsharing.dao.CarDAO;
import carsharing.dao.CompanyDAO;
import carsharing.dao.CustomerDAO;
import carsharing.db.DBClient;
import carsharing.db.H2Client;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;
import carsharing.services.ConsoleInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);;
    private static CompanyDAO companyDAO;
    private static CarDAO carDAO;
    private static CustomerDAO customerDAO;
    private static ConsoleInterface console;

    public static void main(String[] args) {
        // write your code here
        String url = "jdbc:h2:./src/carsharing/db/carsharing";
        for (int index = 0; index < args.length; index++) {
            if (args[index].equals("-databaseFileName")) {
                url = "jdbc:h2:./src/carsharing/db/" + args[index + 1];
            }
        }

        DBClient client = H2Client.getInstance(url);
        // Constructors drop tables, thus need to drop referenced tables first (H2 does not support Cascade)
        carDAO = new CarDAO(client);
        companyDAO = new CompanyDAO(client);
        customerDAO = new CustomerDAO(client);
        console = new ConsoleInterface(scanner, companyDAO, carDAO, customerDAO);
        // Car table references Company table, need to create Company table first
        companyDAO.create();
        // Customer table references Car table, need to create Car table first
        carDAO.create();
        customerDAO.create();

        mainHandler();

        client.disconnect();
    }

    private static void mainHandler() {
        boolean iterate = true;
        while (iterate) {
            console.printMain();
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 1 -> managerHandler();
                case 2 -> customerHandler();
                case 3 -> console.createCustomer();
                default -> iterate = false;
            }
        }
    }

    private static void managerHandler() {
        boolean iterate = true;
        while (iterate) {
            console.printManager();
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 1 -> companiesHandler();
                case 2 -> console.createCompany();
                default -> iterate = false;
            }
        }
    }

    private static void companiesHandler() {
        boolean iterate = true;
        while (iterate) {
            boolean hasCompanies = console.printCompaniesList();
            if (!hasCompanies) {
                return;
            }
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 0 -> iterate = false;
                default -> carsHandler(input);
            }
        }
    }

    private static void carsHandler(int companiesInput) {
        List<Company> companies = companyDAO.findAll();
        // index starts at 0, but console list starts at 1
        Company company = companies.get(companiesInput - 1);

        boolean iterate = true;
        while (iterate) {
            console.printCompany(company.getName());
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 1 -> console.printCarsList(company);
                case 2 -> console.createCar(company);
                default -> {
                    iterate = false;
                    managerHandler();
                }
            }
        }
    }

    private static void customerHandler() {
        boolean iterate = true;
        while (iterate) {
            boolean hasCustomers = console.printCustomersList();
            if (!hasCustomers) {
                return;
            }
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 0 -> iterate = false;
                default -> customersInputHandler(input);
            }
        }
    }

    private static void customersInputHandler(int customersInput) {
        List<Customer> customers = customerDAO.findAll();
        // index starts at 0, but console list starts at 1
        Customer customer = customers.get(customersInput - 1);

        boolean iterate = true;
        while (iterate) {
            console.printCustomer();
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 1 -> {
                    if (customer.getRented_car_id() != null) {
                        System.out.println("\nYou've already rented a car!\n");
                    } else {
                        boolean hasCompanies = console.printCompaniesList();
                        if (!hasCompanies) {
                            continue;
                        }
                        int companiesInput = Integer.parseInt(scanner.nextLine());
                        Company company = companyDAO.findById(companiesInput);
                        List<Car> cars = console.printCarsList(company);
                        if (cars.size() == 0) {
                            continue;
                        }
                        int carsInput = Integer.parseInt(scanner.nextLine());
                        Car car = cars.get(carsInput - 1);
                        customer.setRented_car_id(car.getId());
                        customerDAO.updateById(customer.getId(), customer);
                        System.out.println("\nYou rented '%s'".formatted(car.getName()));
                    }
                }
                case 2 -> {
                    if (customer.getRented_car_id() == null) {
                        System.out.println("\nYou didn't rent a car!");
                    } else {
                        customer.setRented_car_id(null);
                        customerDAO.updateByIdRemoveCar(customer.getId(), customer);
                        System.out.println("\nYou've returned a rented car!\n");
                    }
                }
                case 3 -> console.printMyRentedCar(customer);
                default -> iterate = false;
            }
        }
    }
}