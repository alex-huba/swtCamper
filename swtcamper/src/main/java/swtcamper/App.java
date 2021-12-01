package swtcamper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App extends Application {

  private ConfigurableApplicationContext springContext;
  private FXMLLoader fxmlLoader;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void init() throws Exception {
    springContext = SpringApplication.run(App.class);
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setControllerFactory(springContext::getBean);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    fxmlLoader.setLocation(getClass().getResource("/fxml/mainView.fxml"));
    Parent rootNode = fxmlLoader.load();

    primaryStage.setTitle("SWTCamper");
    //    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setScene(new Scene(rootNode, 800, 600));
    primaryStage.show();

    primaryStage.setOnCloseRequest(
      e -> {
        Platform.exit();
        System.exit(0);
      }
    );
  }

  @Override
  public void stop() {
    springContext.close();
  }
}
