Insurance [![Build Status](https://travis-ci.org/VladimirYushkevich/insurance-service.svg?branch=master)](https://travis-ci.org/VladimirYushkevich/insurance-service) [![Code Coverage](https://img.shields.io/codecov/c/github/VladimirYushkevich/insurance-service/master.svg)](https://codecov.io/github/VladimirYushkevich/insurance-service?branch=master)
=
### Description:

insurance-service is a Spring boot application.
It uses in memory *h2database* in the persistence layer with appropriate domain model. Database populated during start up 
via corresponding **DataLoader**.
For CRUD operations with users/clients **UserController** has been created. All required business logic is in
**InsuranceController**.

#### Task:

Imagine a modular insurance product. People can choose from 4 modules. Each module has a
different selectable coverage and a different mathematical risk.

#### These are the modules:
```
Bike (Coverage 0-3k, Risk 30%)

Jewelry (Coverage 500-10k, Risk 5%)

Electronics (Coverage 500-6k, Risk 35%)

Sports Equipment (Coverage 0-20k, Risk 30%)
```

#### TODO:
a) The user should be able to select the coverage for each module.<br/>
b) The price of the tariff, which is the individual configuration for each customer, should be calculated
based on the risk.

### Run service:
```
./gradlew clean build -i && java -jar build/libs/insurance-0.0.1-SNAPSHOT.jar
```

### Usage:

[In memory DB console](http://localhost:8080/h2-console)  
[SWAGGER](http://localhost:8080/swagger-ui.html)

Or if you prefer CLI.
You can check out available users:
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/user?page=0&size=5&sort=firstName' | jq
```
or module:
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/insurance/modules?page=0&size=5' | jq
```
and then calculate price:
```
curl -X POST localhost:8080/insurance/price -d '{"moduleId": 1, "userId": 1, "moduleCoverage": 2000}' -H 'Content-Type: application/json'
```
```
curl -X POST localhost:8080/insurance/price -d '{"moduleId": 1, "userId": 2, "moduleCoverage": 2000}' -H 'Content-Type: application/json'
```

### Environment

macOS Sierra (version 10.12.1)  
Java(TM) SE Runtime Environment (build 1.8.0_92-b14)
