package ru.covariance.mythkeeperpackager.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import ru.covariance.mythkeeperpackager.packager.WonderdraftSymbolsPackager;

public class AssetPackagerController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private TextField directoryPathTextField;

  @FXML
  private Button pathChooser;

  @FXML
  private Text resultMessage;

  @FXML
  private Button generateButton;

  @FXML
  void onGenerateButtonClicked(MouseEvent event) {
    try {
      resultMessage.setText("Generating...");
      WonderdraftSymbolsPackager.packageDirectory(new File(directoryPathTextField.getText()));
      resultMessage.setText("Done!");
    } catch (IOException e) {
      resultMessage.setText("Something went wrong.");
    }
  }

  @FXML
  void onPathChooserClicked(MouseEvent event) {
    DirectoryChooser dirChooser = new DirectoryChooser();

    File selectedDir = dirChooser.showDialog(null);

    if (selectedDir != null) {
      directoryPathTextField.setText(selectedDir.getAbsolutePath());
    } else {
      resultMessage.setText("Something went wrong.");
    }
  }

  @FXML
  void initialize() {
  }
}
