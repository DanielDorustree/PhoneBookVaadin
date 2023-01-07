# Phone Book - Spring Data JPA CRUD with Vaadin

A simple single table Phone Book with [Spring Data JPA](http://projects.spring.io/spring-data-jpa/) and [Vaadin](https://vaadin.com). Uses [Spring Boot](http://projects.spring.io/spring-boot/) for easy project setup and development. Helps you to get started with basic JPA backed applications and [Vaadin Spring Boot](https://vaadin.com/addon/vaadin-spring-boot) integration library.

## How to run this application

### Suggested method

* Clone the project
* Import to your favorite IDE
* Execute the main method from Application class

### Just execute it with maven

```
git clone https://github.com/mstahv/spring-data-vaadin-phonebook.git
cd spring-data-vaadin-phonebook
mvn spring-boot:run
```

### Just create the package and run it

The built .jar file is auto-runnable, so as you can move it to any computer having java installed and run the app. 

```
git clone https://github.com/mstahv/spring-data-vaadin-phonebook.git
cd spring-data-vaadin-phonebook
mvn package
java -jar target/spring-data-vaadin-phonebook-0.0.1-SNAPSHOT.jar
```