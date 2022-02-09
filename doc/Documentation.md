
# SWTcamper Documentation

## Repository structure

The below overview contains the most relevant files and folders, each accompanied by a brief description of its use.

```plain
.
├── doc
│   ├── Documentation.md %% information on development tools and the implemented software (this document)
│   └── Specification.md %% a textual description of what the software shall do
├── docker-compose.yml %% database service configuration
├── infrastructure %% further database configuration
├── LICENSE %% GNU GPL v3 software license
├── README.md %% a brief summary of this repository
├── .gitignore %% a list of patterns indicating files to be ignored by git
└── swtcamper %% 
    └── ... %% source code of the SWTcamper software
```

## Required software

**Operating system**  

The software should be runnable on all standard operating systems that provide installation candidates for the below mentioned development tools. The software has in particular been tested on Windows 10.

**Required deployment tools**  
* git, see https://git-scm.com/
* Java 11 OpenJDK, see https://jdk.java.net/java-se-ri/11
* Docker, see https://www.docker.com/

## Deployment

1. Install the software mentioned above
2. Create a new ssh key and add it to your gitlab account (see https://gitlab.rz.uni-bamberg.de/profile/keys)
3. Clone this repository
4. Start the database via `docker-compose up database`
5. Navigate to the `swtcamper` directory and start the SWTcamper application via `./gradlew bootRun`

Afterwards, stop the database using `docker-compose down`. In order to remove any data currently stored in the database use `docker-compose down -v`.

## Project structure

The below overview contains the most relevant files and folders, each accompanied by a brief description of its use.

```plain
SWTcamper
|   build.gradle %% The gradle build file
|   gradlew %% The gradle wrapper script
\---src
    +---main
    |   +---java
    |   |   \---swtcamper
    |   |       |   App.java %% Defines the SpringBootApplication
    |   |       |   Main.java %% Starts the application
    |   |       +---api %% Classes which act as intermediary between views and services
    |   |       |   |   ModelMapper.java %% Converts entity objects to DTOs and back
    |   |       |   +---contract %% Data Transfer Objects, used to transfer data between views and services
    |   |       |   |   \---interfaces %% Interfaces of the controller classes, specifiying CRUD and supportive methods
    |   |       |   \---controller %% Backend controllers receive DTOs, invoke services, and return DTOs. Also ValidationHelper, which offers static methods to validate user inputs
    |   |       +---backend %% All resources associated with the backend component
    |   |       |   +---entities %% Represent concepts, some persisted in the database
    |   |       |   |   \---interfaces %% Interfaces of the entity classes
    |   |       |   +---repositories %% Provides basic CRUD operations on entities, e.g., find, save, delete, etc.
    |   |       |   \---services %% Implement the business logic and persist changes made to entities in the database using repositories
    |   |       |       \---exceptions %% Custom exceptions to be thrown
    |   |       \---javafx
    |   |           \---controller %% Controller classes for each view, receive user input and invoke backend controllers
    |   \---resources
    |       |   application.yml %% Spring configuration file
    |       +---fxml %% Provide UI structure in XML-based fxml language
    |       +---icons
    |       +---pictures
    |       \---styles %% CSS files for providing styling to the fxml files
    \---test
        +---java %% Unit and integration tests
        \---resources %% Test data and configuration of the application for testing
```

## Architecture & Design

The SWTcamper application comprises a frontend and a backend component. It relies on an existing mariaDB database. The backend uses Spring Boot, an open-source framework for developing Java applications, and the frontend uses JavaFX, an open-source library for building a UI using an XML-based language.

**api and backend**  
Spring Boot provides means to automatically store and retrieve Java objects into SQL-based databases, i.e., it automatically translates classes and attributes to tables and rows (object-relational mapping). Here, the entity class `Offer` defines a mapping from attributes to columns, whereas the repository class `OfferRepository` provides standard database operations for objects of type `Offer`, such as, `findById`, `findAll`, `save`, and `delete`. The services of the application, e.g., the `OfferService`, operate on the entity level and implement domain specific operations, e.g., method `create` is used to create and persist a new Task object in the database. The controller classes provide a gateway for the services that can be used by the frontend. The frontend does not operate on the entity level, but instead uses DTOs (Data Transfer Objects), e.g., `OfferDTO`. Hence, the controllers, e.g., `OfferController`, map incoming DTO arguments to entities and vice versa. This enforces a clear separation of concerns between the frontend and backend. The reference to other repositories, services, and controllers set automatically by the framework (dependency injection).

**javafx frontend**  
JavaFX enables the developer to store the designed UI in an XML file, e.g., `OfferView.fxml`. At runtime, the application automatically constructs the objects required for rendering the UI. Thereby, the view is strictly separated from the controller logic. However, the controller logic may change the displayed UI, if necessary. In the SWTcamper application, each view is described in a XML file and is associated with a view controller, e.g., `OfferViewController`. The UI components, such as buttons, labels, etc., defined in the XML file can be associated with a unique id and methods to be invoked in case of actions. The fields and methods in the respective view controller are set automatically (dependency injection) to these UI component at runtime. For example, clicking on the button `Angebot buchen` invokes the method `bookingAction` in the `OfferViewController`.
