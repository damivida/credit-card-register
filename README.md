# credit-card-register

### Overview

The application is designed with two main components and two APIs: `card process` and `card register` to simulate card creation process.

1. **Card Register API**:
    - This API is used to record user information necessary for issuing a card.
    - Once the user details are registered, they can be forwarded to the `card process` API for card creation.

2. **Card Process API**:
    - This API handles the creation of cards.
    - The card creation process is simulated at defined time intervals (e.g., one card is generated every 30 seconds).
    - Once a card is generated, the user's OIB  is sent back to the `card register` API via a Kafka topic to update the card status.

### Prerequisites
- Java 17
- Maven
- Kafka

### Running the Application
To build and run the application, go to root folder and use:
```bash
mvn clean install
mvn spring-boot:run
```
### Access API documentation and DB
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- For H2 Console: http://localhost:8080/h2-console
