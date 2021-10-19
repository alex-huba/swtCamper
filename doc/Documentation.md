
# xTASKS Documentation

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
└── xtasks %% 
    └── ... %% source code of the xTASKS software
```

## Required software

**Operating system**  
The software should be runnable on all standard operating systems that provide installation candidates for the below mentioned development tools. The software has in particular been tested on Ubuntu 18.04 LTS and Windows 10.

**Required deployment tools**  
* git, see https://git-scm.com/
* Java 11 OpenJDK, see https://jdk.java.net/java-se-ri/11
* Docker, see https://www.docker.com/

**Further recommended tools for development**  
* IntelliJ IDEA, see https://www.jetbrains.com/de-de/idea/
* Scene Builder, see https://gluonhq.com/products/scene-builder/#download
* node.js, see https://nodejs.org/en/download/
* DBeaver, see https://dbeaver.io/

## Deployment

1. Install the software mentioned above
2. Create a new ssh key and add it to your gitlab account (see https://gitlab.rz.uni-bamberg.de/profile/keys)
3. Clone this repository
4. Start the database via `docker-compose up database`
5. Navigate to the `xtasks` directory and start the xTASKS application via `./gradlew bootRun`

Afterwards, stop the database using `docker-compose down`. In order to remove any data currently stored in the database use `docker-compose down -v`.

## Project structure

The below overview contains the most relevant files and folders, each accompanied by a brief description of its use.

```plain
xTASKS
├── build.gradle %% the gradle build file
├── gradlew %% the gradle wrapper script
├── src
    ├── main
    │   ├── java
    │   │   └── xtasks
    │   │       ├── Main.java %% starts the application
    │   │       ├── App.java %% defines the SpringBootApplication
    │   │       ├── backend %% all resources associated with the backend component
    │   │       │   ├── entity %% represent concepts persisted in the database
    │   │       │   ├── repository %% provides basic CRUD operations on entities, e.g., find, save, delete, etc.
    │   │       │   ├── service %% implement the business logic and persist changes made to entities in the database using repositories
    │   │       │   └── controller %% backend controllers receive DTOs, invoke services, and return DTOs
    │   │       ├── frontend
    │   │       │   └── controller %% frontend controllers map user interaction, e.g., clicking on a button, to backend controller actions, e.g., assigning a task
    │   │       └── shared %% DTO classes shared between the frontend and backend
    │   └── resources %% FXML view files and configuration of the application for deployment
    └── test
        ├── java %% unit and integration tests
        └── resources %% test data and configuration of the application for testing
```

## Architecture & Design

The xTASKS application comprises a frontend and a backend component. It relies on an existing mariaDB database. The backend uses Spring Boot, an open-source framework for developing Java applications, and the frontend uses JavaFX, an open-source library for building a UI using an XML-based language.

**api and backend**  
Spring Boot provides means to automatically store and retrieve Java objects into SQL-based databases, i.e., it automatically translates classes and attributes to tables and rows (object-relational mapping). Here, the entity class `Task` defines a mapping from attributes to columns, whereas the repository class `TaskRepository` provides standard database operations for objects of type `Task`, such as, `findById`, `findAll`, `save`, and `delete`. The services of the application, e.g., the `TaskService`, operate on the entity level and implement domain specific operations, e.g., method `create` is used to create and persist a new Task object in the database. The controller classes provide a gateway for the services that can be used by the frontend. The frontend does not operate on the entity level, but instead uses DTOs (Data Transfer Objects), e.g., `TaskDTO`. Hence, the controllers, e.g., `TaskController`, map incoming DTO arguments to entities and vice versa. This enforces a clear separation of concerns between the frontend and backend. The reference to other repositories, services, and controllers set automatically by the framework (dependency injection).

**javafx frontend**  
JavaFX enables the developer to store the designed UI in an XML file, e.g., `taskCreationView.fxml`. At runtime, the application automatically constructs the objects required for rendering the UI. Thereby, the view is strictly separated from the controller logic. However, the controller logic may change the displayed UI, if necessary. In the xTASKS application, each tab is described in a XML file and is associated with a view controller, e.g., `TaskCreationViewController`. The Ui components, such as buttons, labels, etc., defined in the XML file can be associated with a unique id and methods to be invoked in case of actions. The fields and methods in the respective view controller are set automatically (dependency injection) to these UI component at runtime. For example, clicking on button `Create new Task` invokes the method `createNewTask` in the `TaskCreationViewController`.

## Development

1. Import the project located in the `xtasks` folder as a Gradle project into IntelliJ.
2. Make sure to start the database (see above) before running the xTASKS application (use the gradle task `bootRun`)
3. Make changes to the code and the tests .... 
    - Use scene builder to make changes to the FXML files (you might need to refresh your IntelliJ project afterwards)
    - Rerun the `bootRun` gradle task after making changes.
    - When running your software, inspect the database (=the current state of your application) using DBeaver
    - Run `npm run autoformat` to automatically format your code 
    - Use the `Analyze > Code Cleanup` and Analyze > Inspect Code` feature from IntelliJ.
4. Test your code by running the unit and integration test cases as follows
    - Naming test cases follows the pattern `<method>Should<expected>(If/For<condition>)`

## Build pipeline

The xTASKS project automatically runs three build jobs whenever a new change is pushed to the repository:
- `Checkformat`: checks the format of your code (use the `autoformat.sh`/`autoformat.bat` script before pushing your changes)
- `Compile`: checks if the project compiles
- `Testcoverage`: runs all test cases and computes the code coverage