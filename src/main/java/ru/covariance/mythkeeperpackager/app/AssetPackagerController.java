package ru.covariance.mythkeeperpackager.app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class AssetPackagerController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Text assetPackagerText;

  @FXML
  private Button assetPackagerButton;

  @FXML
  void onAssetPackagerButtonClicked(MouseEvent event) {
    assetPackagerText.setText("*boop*");
  }

  @FXML
  void initialize() {

  }
}
