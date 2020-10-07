package ru.covariance.mythkeeperpackager.app;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Controller {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private AnchorPane basePane;

  @FXML
  private BorderPane corePane;

  @FXML
  private Button homePageButton;

  @FXML
  private Button assetPackagerPageButton;

  @FXML
  private Button mythkeeperPackagerPageButton;

  @FXML
  private BorderPane homePagePane;

  private static final List<String> pageNames = List.of(
      "mythkeeperPackagerPage",
      "assetPackagerPage"
  );
  private static final Map<String, Parent> pages = new HashMap<>();

  private Parent openPage(String page) {
    try {
      return FXMLLoader.load(getClass().getResource("/fxml/" + page + ".fxml"));
    } catch (IOException e) {
      return null;
    }
  }

  private void setPage(String pageName) {
    Parent page = pages.get(pageName);
    if (page == null) {
      corePane.setCenter(homePagePane);
    } else {
      corePane.setCenter(page);
    }
  }

  @FXML
  void onAssetPackagerPageButtonClicked(MouseEvent event) {
    setPage("assetPackagerPage");
  }

  @FXML
  void onHomePageButtonClicked(MouseEvent event) {
    corePane.setCenter(homePagePane);
  }

  @FXML
  void onMythkeeperPackagerPageButtonClicked(MouseEvent event) {
    setPage("mythkeeperPackagerPage");
  }

  @FXML
  void initialize() {
    for (String name : pageNames) {
      Parent page = openPage(name);
      pages.putIfAbsent(name, page);
    }
  }
}
