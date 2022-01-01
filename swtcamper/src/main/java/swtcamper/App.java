package swtcamper;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
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
    primaryStage.setScene(new Scene(rootNode, 1200, 850));
    primaryStage.setMinWidth(950);
    primaryStage.setMinHeight(850);
    primaryStage.getIcons().add(new Image("pictures/logo.png"));
    primaryStage.show();
  }

  @Override
  public void stop() {
    springContext.close();
  }
}
