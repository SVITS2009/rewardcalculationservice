# rewardcalculationservice
This service is responsible to create customers and transactions and calculate the reward point for each customer for the last 3 months.

Reward-point calculation Spring Boot Application

H2 DB (In memory Database) + Spring Boot(3.1.1) + JPA + Maven + Junit5 + Git

Software required for local setup( Without container) -
* install JDK 17
* Maven 3
* Git
* Java IDE (IntelliJ) 

* Clone the project - [git clone https://github.com/SVITS2009/online-shoppinig.git](https://github.com/SVITS2009/rewardcalculationservice.git)

* Go to project directory and run the below commands.
  - Clean - mvn clean
  - Run the test cases - mvn test
  - Run install - mvn clean package 

* Running Application using the below options.
  There are several ways to run a Spring Boot application on your local machine. One way is to
  
  1.) Run the main method in the com.infogain.rewardcalculationservice.RewardCalculationServiceApplication class from your IDE.
  
  2.) Alternatively you can use the Spring Boot Maven plugin like so:
  
      mvn spring-boot:run


* Reward calculation service is running on 8081 port. Once the application is running. 
  Please use the below endpoints to get the swagger document info.
  - To access spring docs UI - [http://localhost:8081/onlineShopping/swagger-ui/](http://localhost:8081/swagger-ui/index.html) 
  - To access spring  docs in Json form - [http://localhost:8081/onlineShopping/v2/api-docs](http://localhost:8081/v3/api-docs)
  - To access H2 DB console - http://localhost:8081/onlineShopping/h2-console

We have reward points for customers and transaction-related APIs like create, update, get customers, and delete customers.
