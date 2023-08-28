# Car Sharing Console App (Java)

A console-based Car Sharing application implemented in Java for the purpose of practicing Java programming, Database management using JDBC, and the implementation of various design patterns.

## Table of Contents

- [Purpose](#purpose)
- [Features](#features)
  - [Design Patterns](#design-patterns)
  - [Database](#database)
- [Models](#models)
- [Interfaces](#interfaces)
- [Services](#services)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)

## Purpose

The primary objective is to:

- Practice Java programming.
- Gain hands-on experience with Database management using JDBC.
- Apply various design patterns like Singleton, DAO, and Generic Interfaces.

## Features

### Design Patterns

- **Singleton**: Ensures that only a single instance of `DBClient` is created. This is primarily used to maintain a single database connection throughout the application's lifetime.
  
- **Data Access Object (DAO)**: Encapsulates all database-related CRUD operations. Dependency injection is manually done to inject the `DBClient` instance from `Main` to the required classes.

- **Generic Interface for DB**: Provides a loosely coupled design between the application and the database, allowing the database to be switched easily without affecting other parts of the code.

### Database

- **H2 Database**: A lightweight, in-memory database is used.
- **Database Operations**: The application performs `CREATE`, `INSERT`, `UPDATE`, `DELETE`, and `SELECT` SQL operations.
- **Truncation**: All tables are truncated at the end of the application's execution to ensure no lingering data can interfere with subsequent runs or tests.

## Models

- **Customer**: Fields include `id`, `name`, `rented_car_id`.
- **Company**: Fields include `id`, `name`.
- **Car**: Fields include `id`, `name`, `company_id`.

## Interfaces

- **DB Interface**: Includes methods for `run(String sql)`, `select(String sql, Consumer<ResultSet> handler)`, and `disconnect()`.
  
- **DAO Interface**: Includes methods for `create()`, `insert()`, `updateById()`, `deleteById()`, `findAll()`, and `findById()`.

## Services

- **ConsoleInterface**: Manages all user interactions through a console-based UI.

## Getting Started

### Prerequisites

- Java JDK 8 or above
- H2 Database

### Installation

1. **Clone the Repo**
    ```bash
    git clone https://github.com/n-alonso/JAVA-console_based_jdbc_service.git
    ```
    
2. **Navigate to Project Directory**
    ```bash
    cd JAVA-console_based_jdbc_service
    ```
    
3. **Compile Java Files**
    ```bash
    javac -d out src/*.java
    ```

4. **Run the Main Class**
    ```bash
    java -cp out Main
    ```

## Usage

Run the application and follow the on-screen instructions to manage companies, cars, and customers.
