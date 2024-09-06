# Transaction Service
This service is an assessment based use case. The aim to build a service which can handle transfers and transactions at large  
### Transaction service ###

* Name: Transaction Service
* Version: 0.0.1

## Application Requirements
- The full overview of the requirement can be found [here](UseCase.pdf).

## Architechtural Choice
This project is built using the ### Layered architecture ### [Layered Architecture](layered-architecture.webp)

### Technologies used ###

The technologies used for developing this application includes:
* Java 21
* Spring boot 3.3
* Flyway (For database migrations)
* Postgres Database
* JOOQ (for the persistence layer and dynamic query). see documentation [here](https://www.jooq.org/)
* docker compose for running in local environment
* github for version control and application deployment
* Rabbit MQ

### To set up on local environment ###

* Ensure docker is installed and running on your machine [docker](https://www.docker.com/)
* [Git](https://git-scm.com/)
* [OpenJDK 21](https://adoptium.net/temurin/releases)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) or any other suitable IDE of choice
* configure the environment variables like the following
    - TRANSFERSERVICE_DATASOURCE_URL=jdbc:postgresql://localhost:5432/postgres
    - TRANSFERSERVICE_DATASOURCE_USERNAME=postgres
    - TRANSFERSERVICE_DATASOURCE_PASSWORD=postgres
    - REM-RABBIT-MQ-HOST=localhost
    - REM-RABBIT-MQ-PORT=5672
    - REM-RABBIT-MQ-USERNAME=user
    - REM-RABBIT-MQ-PASSWORD=password
    - TRANSACTIONS_COMMISSIONS_CRON=0 */5 * * * ?
    - TRANSACTIONS_COMMISSIONS_VALUE=20
    - TRANSACTIONS_VALUE=0.5
    - TRANSACTIONS_AMOUNT_CAP=100
    - TRANSACTIONS_SUMMARY_CRON=0 */5 * * * ?

### Running locally ####
The code contains a docker compose file [docker-compose](compose.yaml). This quickly builds the service and spin up a postgres database to run the application locally
To run the app, do the following:
- navigate to the directory of the project
- run `docker compose up`
### Testing Locally ###
The code contains a [api-docs.yml](./api-docs.yml) to test locally. you can also visit [swagger page](http://localhost:8080/swagger-ui.html)

### Who do I talk to? ###

* [Babatunde Lawal](robinlittle51@gmail.com)
