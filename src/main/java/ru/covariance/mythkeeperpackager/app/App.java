package ru.covariance.mythkeeperpackager.app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
    primaryStage.setTitle("MythkeeperPackager");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}
