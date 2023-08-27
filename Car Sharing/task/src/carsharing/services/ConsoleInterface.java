package carsharing.services;

import carsharing.dao.CarDAO;
import carsharing.dao.CompanyDAO;
import carsharing.dao.CustomerDAO;
import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ConsoleInterface {

    private final Scanner scanner;
    private final CompanyDAO companyDao;
    private final CarDAO carDao;
    private final CustomerDAO customerDao;

    public ConsoleInterface(Scanner scanner, CompanyDAO companyDao, CarDAO carDao, CustomerDAO customerDao) {
        this.scanner = scanner;
        this.companyDao = companyDao;
        this.carDao = carDao;
        this.customerDao = customerDao;
    }

    private final String MAIN_MENU = """
            \n1. Log in as a manager
            2. Log in as a customer
            3. Create a customer
            0. Exit\n""";

    private final String MANAGER_MENU = """
            \n1. Company list
            2. Create a company
            0. Back\n""";

    private final String COMPANY_MENU = """
            \n'%s' company:
            1. Car list
            2. Create a car
            0. Back\n""";

    private final String CUSTOMER_MENU = """
            \n1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back\n""";

    public void printMain() {
        System.out.println(MAIN_MENU);
    }

    public void printManager() {
        System.out.println(MANAGER_MENU);
    }

    public void printCompany(String companyName) {
        System.out.println(COMPANY_MENU.formatted(companyName));
    }

    public void printCustomer() {
        System.out.println(CUSTOMER_MENU);
    }

    public void createCompany() {
        System.out.println("\nEnter the company name:");

        String name = scanner.nextLine();
        Company newCompany = new Company(name);
        companyDao.insert(newCompany);

        System.out.println("The company was created!");
    }

    public boolean printCompaniesList() {
        List<Company> companies = companyDao.findAll();

        if (companies.size() == 0) {
            System.out.println("\nThe company list is empty!");
            return false;
        } else {
            System.out.println("\nChoose a company:");
            companies.stream().forEach(company -> {
                System.out.println((company.getId()) + ". " + company.getName());
            });
            System.out.println("0. Back\n");
            return true;
        }
    }

    public void createCar(Company company) {
        System.out.println("\nEnter the car name:");

        String name = scanner.nextLine();
        Car newCar = new Car(name, company.getId());
        carDao.insert(newCar);

        System.out.println("The car was created!");
    }

    public List<Car> printCarsList(Company company) {
        List<Car> cars = carDao.findAll();
        List<Car> companyCars = cars.stream()
                .filter(car -> car.getCompany_id() == company.getId())
                .toList();

        if (companyCars.size() == 0) {
            System.out.println("\nThe car list is empty!");
        } else {
            System.out.println("\n'%s' cars:".formatted(company.getName()));
            AtomicInteger index = new AtomicInteger(1);
            companyCars.stream().forEach(car -> {
                System.out.println(index.getAndIncrement() + ". " + car.getName());
            });
            System.out.println("0. Back");
        }

        return cars;
    }

    public void createCustomer() {
        System.out.println("\nEnter the customer name:");

        String name = scanner.nextLine();
        Customer newCustomer = new Customer(name, null);
        customerDao.insert(newCustomer);

        System.out.println("The customer was created!");
    }

    public boolean printCustomersList() {
        List<Customer> customers = customerDao.findAll();

        if (customers.size() == 0) {
            System.out.println("\nThe customer list is empty!");
            return false;
        } else {
            System.out.println("\nChoose a customer:");
            customers.stream().forEach(customer -> {
                System.out.println((customer.getId()) + ". " + customer.getName());
            });
            System.out.println("0. Back\n");
            return true;
        }
    }

    public boolean printMyRentedCar(Customer customer) {
        if (customer.getRented_car_id() == null) {
            System.out.println("\nYou didn't rent a car!\n");
            return false;
        }
        Car rentedCar = carDao.findById(customer.getRented_car_id());
        if (rentedCar != null) {
            Company company = companyDao.findById(rentedCar.getCompany_id());
            System.out.println("""
                \nYour rented car:
                %s
                Company:
                %s\n""".formatted(rentedCar.getName(), company.getName()));
            return true;
        } else {
            System.out.println("\nYou didn't rent a car!\n");
            return false;
        }
    }
}
