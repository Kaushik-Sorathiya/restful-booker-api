# Restful Booker API Test Framework

## 1) Project Description
API Test automation framework using Java, Rest Assured, TestNG, and Extent Reports for the Restful Booker API.

## 2) Technologies Used
- Java
- Maven
- TestNG
- Rest Assured
- Extent Reports

## 3) Folder Structure
```
restful-booker-api/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── utils/
│   │           ├── ApiUtil.java
│   │           ├── AuthUtil.java
│   │           ├── Booking.java
│   │           ├── BookingDates.java
│   │           ├── LogUtil.java
│   │           └── TestDataUtil.java
│   └── test/
│       └── java/
│           ├── base/
│           │   └── BaseTest.java
│           └── tests/
│               └── BookingTests.java
├── test-output/
│   └── ExtentReport.html
├── Jenkinsfile
├── pom.xml
└── testng.xml
```

## 4) Setup Instructions

### a) Prerequisites
Make sure you have the following installed:

- [Java JDK 11+](https://adoptopenjdk.net/)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) or another Java IDE

### b) Clone the Project
```bash
git clone https://github.com/Kaushik-Sorathiya/restful-booker-api.git
cd restful-booker-api
```

### c) Import into IntelliJ
1. Open IntelliJ IDEA.
2. Select **File > Open** and choose the folder `restful-booker-api`.
3. IntelliJ will automatically import it as a Maven project.
4. Ensure the **Project SDK** is set (Java 11+).
5. Let Maven download all dependencies.

## 5) Number of Tests
The framework currently includes **5 test cases** in the `BookingTests` class, covering:
- Retrieving all bookings (GET /booking)
- Creating a new booking (POST /booking)
- Retrieving a booking by ID (GET /booking/{id})
- Updating a booking (PUT /booking/{id})
- Deleting a booking (DELETE /booking/{id})

## 6) How to Run Tests
From the project root directory, run:
```bash
mvn clean test
```

## 7) How to View Reports
After tests are executed, open the generated Extent Report in your browser:
```
target/ExtentReport.html
```

## 8) Run with Jenkins (Optional CI)

This project includes a `Jenkinsfile`. To run in Jenkins:

1. Create a new Pipeline job in Jenkins.
2. In the pipeline configuration, use "Pipeline script from SCM" and connect your GitHub repository.
3. Ensure Jenkins is configured with JDK and Maven.
4. Add the **HTML Publisher Plugin** to view Extent Reports after builds.
