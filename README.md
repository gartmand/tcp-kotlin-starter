# tcp-starter-kotlin

A CRUD example using Spring WebFlux in Kotlin

##### 1. Install [docker](https://docs.docker.com/engine/installation/) and [docker-compose](https://docs.docker.com/compose/install/)

##### 2. Run the application

- Compile the app: `./start clean build`

- Run the application: `./start bootrun`

##### 3. Play with data

The app currently only has one document spec (Employees).

I haven't built out a Skills or Categories spec yet.

Since I'm using functional endpoints, Swagger will have to be a manual spec
(unless switching to annotation-based routing). Currently it's pretty easy to tell what the schema
is from the domain models, though.

##### 4. Set up IntelliJ
- Import the project
    1. Select `Import Project`
    2. Select the directory where you cloned the repository
    3. Import as a gradle project and select a JDK >= 1.8
    4. Select to use the gradle wrapper configuration from the project
