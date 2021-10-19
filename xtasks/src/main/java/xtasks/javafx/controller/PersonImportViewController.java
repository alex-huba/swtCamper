package xtasks.javafx.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.controller.PersonController;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class PersonImportViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private PersonController personController;

  @FXML
  public TextField importPath;

  @FXML
  public void importButtonAction() {
    String importPathString = importPath.getText();
    if (importPathString.isEmpty()) {
      mainViewController.handleExceptionMessage(
        "The prior selected file path is empty."
      );
      return;
    }
    Path path = Paths.get(importPathString);
    if (!Files.exists(path)) {
      mainViewController.handleExceptionMessage(
        String.format(
          "The prior selected file \"%s\" does not exists.",
          path.toAbsolutePath()
        )
      );
      return;
    }

    String json;
    try {
      json = Files.readString(path);
    } catch (IOException e) {
      mainViewController.handleExceptionMessage(
        String.format(
          "An error occurred while trying to read file %s",
          path.toAbsolutePath()
        )
      );
      return;
    }
    try {
      personController.importPeopleFromJSON(json);
      mainViewController.handleInformationMessage("The import was successful!");
      mainViewController.reloadData();
    } catch (GenericServiceException e) {
      mainViewController.handleException(e);
      return;
    }
  }

  @FXML
  public void importFileChooserAction(ActionEvent event) {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File file = fileChooser.showOpenDialog(window);
    if (file == null) {
      return;
    }
    importPath.setText(file.getAbsolutePath().toString());
  }
}
