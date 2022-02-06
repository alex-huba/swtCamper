
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

The below overview contains all files and folders, each accompanied by a brief description of its use.

```plain
SWTcamper
|   build.gradle
|   gradlew
\---src
    +---main
    |   +---java
    |   |   \---swtcamper
    |   |       |   App.java %% defines the SpringBootApplication
    |   |       |   Main.java %% starts the application
    |   |       |
    |   |       +---api
    |   |       |   |   ModelMapper.java
    |   |       |   |
    |   |       |   +---contract %% Data Transfer Objects, used to transfer data between View and Service
    |   |       |   |   |   BookingDTO.java
    |   |       |   |   |   LoggingMessageDTO.java
    |   |       |   |   |   OfferDTO.java
    |   |       |   |   |   OfferedObjectTypeDTO.java
    |   |       |   |   |   PictureDTO.java
    |   |       |   |   |   UserDTO.java
    |   |       |   |   |   UserReportDTO.java
    |   |       |   |   |   UserRoleDTO.java
    |   |       |   |   |   VehicleDTO.java
    |   |       |   |   |   VehicleTypeDTO.java
    |   |       |   |   |
    |   |       |   |   \---interfaces %% Interfaces of the controller classes, specifiying CRUD and supportive methods
    |   |       |   |           IBookingController.java
    |   |       |   |           ILoggingController.java
    |   |       |   |           IOfferController.java
    |   |       |   |           IPictureController.java
    |   |       |   |           IUserController.java
    |   |       |   |           IUserReportController.java
    |   |       |   |
    |   |       |   \---controller %% Backend controllers receive DTOs, invoke services, and return DTOs
    |   |       |           BookingController.java
    |   |       |           HashHelper.java
    |   |       |           LoggingController.java
    |   |       |           OfferController.java
    |   |       |           PictureController.java
    |   |       |           UserController.java
    |   |       |           UserReportController.java
    |   |       |           ValidationHelper.java %% Offers static methods to validate user inputs
    |   |       |
    |   |       +---backend %% All resources associated with the backend component
    |   |       |   +---entities %% Represent concepts, some persisted in the database
    |   |       |   |   |   Booking.java
    |   |       |   |   |   Filter.java %% Records chosen filter options for getting fitting offers
    |   |       |   |   |   FuelType.java
    |   |       |   |   |   LoggingLevel.java
    |   |       |   |   |   LoggingMessage.java
    |   |       |   |   |   Offer.java
    |   |       |   |   |   OfferedObjectType.java
    |   |       |   |   |   Picture.java
    |   |       |   |   |   TransmissionType.java
    |   |       |   |   |   User.java
    |   |       |   |   |   UserReport.java
    |   |       |   |   |   UserRole.java
    |   |       |   |   |   Vehicle.java
    |   |       |   |   |   VehicleType.java
    |   |       |   |   |
    |   |       |   |   \---interfaces
    |   |       |   |           IBooking.java
    |   |       |   |           IFilter.java
    |   |       |   |           ILoggingMessage.java
    |   |       |   |           IOffer.java
    |   |       |   |           IPicture.java
    |   |       |   |           IUser.java
    |   |       |   |           IUserReport.java
    |   |       |   |           IVehicle.java
    |   |       |   |
    |   |       |   +---repositories %% Provides basic CRUD operations on entities, e.g., find, save, delete, etc.
    |   |       |   |       BookingRepository.java
    |   |       |   |       LoggingRepository.java
    |   |       |   |       OfferRepository.java
    |   |       |   |       PictureRepository.java
    |   |       |   |       UserReportRepository.java
    |   |       |   |       UserRepository.java
    |   |       |   |       VehicleRepository.java
    |   |       |   |
    |   |       |   \---services %% Implement the business logic and persist changes made to entities in the database using repositories
    |   |       |       |   BookingService.java
    |   |       |       |   LoggingService.java
    |   |       |       |   OfferService.java
    |   |       |       |   PictureService.java
    |   |       |       |   UserReportService.java
    |   |       |       |   UserService.java
    |   |       |       |
    |   |       |       \---exceptions
    |   |       |               GenericServiceException.java
    |   |       |               UserDoesNotExistException.java
    |   |       |               WrongPasswordException.java
    |   |       |
    |   |       \---javafx
    |   |           \---controller
    |   |                   AccountViewController.java
    |   |                   ApproveNewProvidersViewController.java
    |   |                   DealHistoryViewController.java
    |   |                   ExcludeRenterViewController.java
    |   |                   LoginViewController.java
    |   |                   MainViewController.java
    |   |                   ModifyOfferViewController.java
    |   |                   MyBookingsViewController.java
    |   |                   MyOffersViewController.java
    |   |                   NavigationViewController.java
    |   |                   OfferViewController.java
    |   |                   RegisterViewController.java
    |   |                   RentingViewController.java
    |   |                   ReportUserViewController.java
    |   |                   ResetPasswordViewController.java
    |   |
    |   \---resources
    |       |   application.yml
    |       |
    |       +---fxml
    |       |       accountView.fxml
    |       |       approveNewProvidersView.fxml
    |       |       dealHistoryView.fxml
    |       |       excludeRenterView.fxml
    |       |       faqView.fxml
    |       |       loginView.fxml
    |       |       mainView.fxml
    |       |       modifyOfferView.fxml
    |       |       myBookingsView.fxml
    |       |       myOffersView.fxml
    |       |       navigationView.fxml
    |       |       offerView.fxml
    |       |       registerView.fxml
    |       |       rentingView.fxml
    |       |       reportUserView.fxml
    |       |       resetPasswordView.fxml
    |       |
    |       +---icons
    |       |       active_offers.png
    |       |       add_photo.png
    |       |       approvement.png
    |       |       deal_history.png
    |       |       delete.png
    |       |       exclude.png
    |       |       faq.png
    |       |       homepage.png
    |       |       login.png
    |       |       logout.png
    |       |       manage.png
    |       |       manage_rental_conditions.png
    |       |       manage_users.png
    |       |       manage_vechicle_feature.png
    |       |       my_bookings.png
    |       |       new_offer.png
    |       |       promote.png
    |       |       user.png
    |       |
    |       +---pictures
    |       |       campervan-wallpaper.jpg
    |       |       logo.png
    |       |       noImg.png
    |       |       SWTCamper.jpg
    |       |
    |       \---styles
    |               deal-history-view.css
    |               exclude-renter-view.css
    |               faq-view.css
    |               global.css
    |               login-view.css
    |               main-view.css
    |               modify-offer-view.css
    |               my-bookings-view.css
    |               my-offers-view.css
    |               navigation-view.css
    |               register-view.css
    |               renting-view.css
    |               report-user-view.css
    |               reset-password-view.css
    |               view-offer-view.css
    |
    \---test
        +---java %% unit and integration tests
        \---resources %% test data and configuration of the application for testing
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