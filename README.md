# Java Inventory Management

Desktop inventory management application developed in Java with Swing, JDBC and MySQL.

The project provides a graphical interface for managing databases, inventory tables and products, with CSV import/export support and a layered structure based on MVC and DAO patterns.

## Features

- Connect to a MySQL server using user-provided credentials
- Create, select, empty and delete databases
- Create, select, empty and delete inventory tables
- Add, view, update and delete products
- Filter and sort inventory data
- Import products from CSV files
- Export products to CSV files
- Validate dynamic SQL identifiers before using them in database operations
- Manage JDBC resources with try-with-resources

## Tech Stack

- Java 17+
- Swing
- JDBC
- MySQL
- Maven
- JUnit 5

## Architecture

The application is organized using a layered approach:

```text
src/main/java
├── controller
├── dao
├── main
├── model
├── util
└── view
```

The main responsibilities are separated as follows:

- **View** — Swing graphical user interface
- **Controller** — coordination between UI and application logic
- **DAO** — database access and persistence operations
- **Model** — inventory domain objects and enumerations
- **Util** — validation, filtering and sorting utilities

## Requirements

Before running the application, install:

- JDK 17 or newer
- Apache Maven
- MySQL Server

A running MySQL server is required.

## Run the application

Clone the repository and move into the project directory:

```bash
git clone https://github.com/ainsana/java-inventory-management.git
cd java-inventory-management
```

Run the tests:

```bash
mvn clean test
```

Start the desktop application:

```bash
mvn exec:java
```

At startup, enter your MySQL connection information.

The database field can be left empty to connect directly to the MySQL server and create or select a database from the application.

## CSV Import and Export

Products can be exported to and imported from semicolon-separated CSV files.

Example:

```text
Product Name;CATEGORY;SIZE;TYPE;COLOR;10;5.0;9.99
```

The product ID is not exported. When a product is imported, MySQL generates a new ID using the table's `AUTO_INCREMENT` primary key.

## Testing

The project uses JUnit 5.

Run the automated tests with:

```bash
mvn clean test
```

The current test suite includes validation tests for dynamic SQL identifiers.

## Database Safety

Database credentials are supplied at runtime and are not stored in the repository or persisted in the application database.

Dynamic database and table identifiers are validated before being used in SQL statements.

JDBC connections, statements and result sets are managed using try-with-resources.

## Documentation

Selected UML diagrams aligned with the current implementation are available in the [`docs/diagrams`](docs/diagrams) directory:

- product model and validation constraints;
- product filtering and sorting flow;
- inventory table selection flow.

## Project Status

This project was originally developed as a Java desktop application and has since been updated with:

- a reproducible Maven build;
- SQL identifier validation;
- improved database credential handling;
- per-operation JDBC connection management;
- automated tests;
- Maven-based application startup.

The current version is functionally complete and maintained as a portfolio project. Future improvements may include broader automated test coverage and additional documentation.
