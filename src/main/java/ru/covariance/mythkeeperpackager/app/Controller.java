package ru.covariance.mythkeeperpackager.app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private TextField packageName;

  @FXML
  private TextField versionTextField;

  @FXML
  private TextField authorTextField;

  @FXML
  private Button submitButton;

  @FXML
  void onSubmitButton(ActionEvent event) {
    System.out.println("Package name: " + packageName.getText());
    System.out.println("Version: " + versionTextField.getText());
    System.out.println("Author: " + authorTextField.getText());
  }

  @FXML
  void initialize() {
    assert packageName != null
        : "fx:id=\"packageNameId\" was not injected: check your FXML file 'packager.fxml'.";
    assert versionTextField != null
        : "fx:id=\"versionTextField\" was not injected: check your FXML file 'packager.fxml'.";
    assert authorTextField != null
        : "fx:id=\"authorTextField\" was not injected: check your FXML file 'packager.fxml'.";
    assert submitButton != null
        : "fx:id=\"submitButton\" was not injected: check your FXML file 'packager.fxml'.";

  }
}
