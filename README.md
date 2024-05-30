# Order Delivery API

## MVP Stories:
### 1. Story 1: Choosing the Delivery Method
**Description:** As a customer, I can choose my delivery method. The available delivery methods are: 'DRIVE', 'DELIVERY', 'DELIVERYTODAY', 'DELIVERYASAP'.

**Acceptance Criteria:**
1. **Scenario: Displaying delivery methods:**
    - **Given** a customer has selected a store,
    - **When** they view the delivery options,
    - **Then** they see all the available delivery methods for that specific store ('DRIVE', 'DELIVERY', 'DELIVERYTODAY', 'DELIVERYASAP').

2. **Scenario: Validating availability:**
    - **Given** a customer has selected a store,
    - **When** they choose a delivery method from the options provided,
    - **Then** the system checks the availability of the selected delivery method for the chosen store,
    - **And** returns the calendar of available delivery slots for the next 15 days.

3. **Scenario: Delivery method unavailable:**
    - **Given** a customer has selected a store,
    - **When** they choose a delivery method that becomes unavailable,
    - **Then** the system returns an exception to inform that the delivery method is unavailable.

### 2. Story 2: Choosing the Delivery Day and Time Slot
**Description:** As a customer, I can choose the day and time slot for my delivery. The time slots are specific to the delivery method and can be booked by other customers.

**Acceptance Criteria:**
1. **Scenario: Booking a time slot:**
    - **Given** a customer has selected a time slot,
    - **When** they book the time slot,
    - **Then** the selected time slot is reserved for this customer,
    - **And** a delivery is created.

2. **Scenario: Desired time slot unavailable:**
    - **Given** a customer has selected a delivery method, a day, and a time slot,
    - **And** the time slot is no longer available,
    - **When** they book the time slot,
    - **Then** the system checks the availability,
    - **And** the system returns an exception to inform that the time slot is no longer available.

### Prerequisites
- Java 21
- Maven
- Git

### Clone repository

   ```
   git clone https://github.com/amousdev/kata-order-delivery.git
   ```
### RUN APPLICATION

From a terminal window, run this command in project root directory: \
    ```  
    mvn spring-boot:run
    ```

### ACCES CONSOLE DB
DB is accessible from http://localhost:8787/h2-console

### ACCESS SWAGGER
Swagger is availavle from URL http://localhost:8181/swagger-ui.html
