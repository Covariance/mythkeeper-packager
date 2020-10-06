package ru.covariance.mythkeeperpackager.app;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ru.covariance.mythkeeperpackager.packager.License;

public class MythkeeperPackagerController {

  /**
   *  Constructor that fills essential fields of Mythkeeper packager page controller.
   */
  public MythkeeperPackagerController() {
    // region LICENSE_MENUS FILLING
    // region NONE LICENSE
    Text noneLicenseNode = new Text();
    noneLicenseNode.setFont(Font.font(22));
    // endregion

    // region PREDEFINED LICENSE
    Node predefinedNode;
    if (License.SUPPORTED_LICENSES.isEmpty()) {
      Text predefined = new Text("No predefined licenses supported.");
      predefined.setFont(Font.font(15));

      predefinedNode = predefined;
    } else {
      Text note = new Text("Choose license:");
      note.setFont(Font.font(15));

      ChoiceBox<String> predefinedLicenses = new ChoiceBox<>();
      predefinedLicenses.getItems().addAll(License.SUPPORTED_LICENSES);
      predefinedLicenses.setValue(License.SUPPORTED_LICENSES.get(0));
      predefinedLicenses.setPrefWidth(350);
      predefinedLicenses.setPrefHeight(Region.USE_COMPUTED_SIZE);

      HBox predefined = new HBox();
      predefined.setSpacing(10);
      predefined.getChildren().addAll(note, predefinedLicenses);

      predefinedNode = predefined;
    }
    // endregion

    // region LOCAL LICENSE
    TextField localLicensePath = new TextField();
    localLicensePath.setPromptText("License path");
    localLicensePath.setPrefWidth(378);
    localLicensePath.setPrefHeight(26);

    Button localPathButton = new Button();
    localPathButton.setText("Choose...");
    localPathButton.setPrefWidth(100);
    localPathButton.setPrefHeight(26);
    localPathButton.setOnMouseClicked(
        mouseEvent -> onLocalLicenseChooseButtonClicked(mouseEvent, localLicensePath)
    );

    HBox localNode = new HBox();
    localNode.setSpacing(0);
    localNode.getChildren().addAll(localLicensePath, localPathButton);
    // endregion

    // region EXTERNAL LICENSE
    Text externalLicenseNote = new Text("License URL:");
    externalLicenseNote.setFont(Font.font(15));

    TextField externalLicense = new TextField();
    externalLicense.setPromptText("e.g. covariance.com/license.pdf");
    externalLicense.setPrefWidth(371);
    externalLicense.setPrefHeight(26);

    HBox externalNode = new HBox();
    externalNode.setSpacing(10);
    externalNode.getChildren().addAll(externalLicenseNote, externalLicense);
    // endregion

    licenseMenus = Map.of(
        "None", noneLicenseNode,
        "Predefined", predefinedNode,
        "Local file", localNode,
        "External link", externalNode
    );
    // endregion

    // region COMMERCIAL_MENUS FILLING
    // region NONE COMMERCIAL
    Text noneCommercialNode = new Text();
    noneCommercialNode.setFont(Font.font(22));
    // endregion

    // region IS COMMERCIAL
    Text commercialUrlHint = new Text("Commercial URL:");
    commercialUrlHint.setFont(Font.font(15));

    TextField commercialUrl = new TextField();
    commercialUrl.setPromptText("e.g. covariance.com/commercial");
    commercialUrl.setPrefWidth(300);
    commercialUrl.setPrefHeight(26);

    HBox commercialUrlNode = new HBox();
    commercialUrlNode.setSpacing(10);
    commercialUrlNode.getChildren().addAll(commercialUrlHint, commercialUrl);
    // endregion

    commercialMenus = Map.of(
        Boolean.FALSE, noneCommercialNode,
        Boolean.TRUE, commercialUrlNode
    );
    // endregion
  }

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private TextField outputPathTextField;

  @FXML
  private Button outputPathChooser;

  @FXML
  private TextField nameTextField;

  @FXML
  private TextField versionTextField;

  @FXML
  private TextField authorNameTextField;

  @FXML
  private TextField authorMailTextField;

  @FXML
  private TextField authorUrlTextField;

  @FXML
  private TextField authorDonationUrlTextField;

  @FXML
  private TextArea descriptionTextArea;

  @FXML
  private ChoiceBox<String> licenseChoiceBox;

  @FXML
  private BorderPane licenseMenuPane;

  @FXML
  private CheckBox isCommercialUseAllowed;

  @FXML
  private BorderPane commercialUseMenuPane;

  @FXML
  private VBox tagListVBox;

  @FXML
  private TextField newTagTestField;

  @FXML
  private Button addNewTagButton;

  @FXML
  private Button packageButton;

  @FXML
  private Text packagingResultMessage;


  @FXML
  void onAddNewTagButtonClicked(MouseEvent event) {

  }

  @FXML
  void onOutputPathChooserClicked(MouseEvent event) {

  }

  @FXML
  void onPackageButtonClicked(MouseEvent event) {

  }

  void onLocalLicenseChooseButtonClicked(MouseEvent event, TextField outField) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("PDF and text files", "*.pdf", "*.txt", "*.doc", "*.docx"),
        new ExtensionFilter("Other files", "*.*")
    );

    File selectedFile = fileChooser.showOpenDialog(null);

    if (selectedFile != null) {
      outField.setText(selectedFile.getAbsolutePath());
    } else {
      outField.setText("Cannot open chosen file.");
    }
  }

  private final Map<String, Node> licenseMenus;

  void changeLicenseMenu(String val) {
    licenseMenuPane.setLeft(licenseMenus.get(val));
  }

  private final Map<Boolean, Node> commercialMenus;

  void changeCommercialMenu(Boolean isCommercial) {
    commercialUseMenuPane.setLeft(commercialMenus.get(isCommercial));
  }

  @FXML
  void initialize() {
    licenseChoiceBox.getItems().addAll("None", "Predefined", "Local file", "External link");
    licenseChoiceBox.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, from, to) -> changeLicenseMenu(to));
    licenseChoiceBox.setValue("None");
    changeLicenseMenu("None");

    isCommercialUseAllowed.selectedProperty()
        .addListener((observableValue, from, to) -> changeCommercialMenu(to));
    changeCommercialMenu(Boolean.FALSE);
  }
}
