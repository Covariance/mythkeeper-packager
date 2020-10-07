package ru.covariance.mythkeeperpackager.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ru.covariance.mythkeeperpackager.packager.Author;
import ru.covariance.mythkeeperpackager.packager.License;
import ru.covariance.mythkeeperpackager.packager.Packager;

public class MythkeeperPackagerController {

  /**
   * Constructor that fills essential fields of Mythkeeper packager page controller.
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
  private TextField newTagTextField;

  @FXML
  private Button addNewTagButton;

  @FXML
  private VBox assetListBox;

  @FXML
  private TextField assetPathTextField;

  @FXML
  private Button assetListDirectoryChooser;

  @FXML
  private Button addAssetDirectoryToList;

  @FXML
  private Button packageButton;

  @FXML
  private Text packagingResultMessage;

  @FXML
  void onOutputPathChooserClicked(MouseEvent event) {
    File selectedDirectory = new DirectoryChooser().showDialog(null);

    outputPathTextField.setText(selectedDirectory == null
        ? "Cannot open chosen directory"
        : selectedDirectory.getAbsolutePath()
    );
  }

  void onLocalLicenseChooseButtonClicked(MouseEvent event, TextField outField) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("PDF and text files", "*.pdf", "*.txt", "*.doc", "*.docx"),
        new ExtensionFilter("Other files", "*.*")
    );

    File selectedFile = fileChooser.showOpenDialog(null);

    outField.setText(selectedFile == null
        ? "Cannot open chosen file"
        : selectedFile.getAbsolutePath()
    );
  }

  @FXML
  void onAddNewTagButtonClicked(MouseEvent event) {
    if (newTagTextField.getText().isBlank()) {
      return;
    }
    addRemovableItem(newTagTextField, tagListVBox);
  }

  @FXML
  void onAssetListDirectoryChooserClicked(MouseEvent event) {
    File selectedDirectory = new DirectoryChooser().showDialog(null);

    assetPathTextField.setText(selectedDirectory == null
        ? "Cannot open chosen directory"
        : selectedDirectory.getAbsolutePath()
    );
  }

  @FXML
  void onAddAssetDirectoryToListClicked(MouseEvent event) {
    if (assetPathTextField.getText().isBlank()) {
      return;
    }
    File input = new File(assetPathTextField.getText());
    if (!input.exists() || !input.isDirectory()) {
      assetPathTextField.setText("Invalid directory!");
      return;
    }
    addRemovableItem(assetPathTextField, assetListBox);
  }

  @FXML
  void onPackageButtonClicked(MouseEvent event) {
    File outputDirectory = new File(outputPathTextField.getText());
    if (outputPathTextField.getText().isBlank()) {
      packagingResultMessage.setText("Output directory must be specified!");
      return;
    }
    if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
      packagingResultMessage.setText("Output directory is invalid!");
      return;
    }

    String name = nameTextField.getText();
    if (name.isBlank()) {
      packagingResultMessage.setText("Package name must be specified!");
      return;
    }

    String version = versionTextField.getText();
    if (version.isBlank()) {
      packagingResultMessage.setText("Version must be specified!");
      return;
    }

    String authorName = authorNameTextField.getText();
    if (authorName.isBlank()) {
      packagingResultMessage.setText("Author name must be specified!");
      return;
    }

    if (assetListBox.getChildren().isEmpty()) {
      packagingResultMessage.setText("There is no sprite directories specified!");
      return;
    }
    List<File> assetDirectories = new ArrayList<>();
    try {
      for (Node boxedDirectory : assetListBox.getChildren()) {
        File dir = new File(((Text) ((HBox) boxedDirectory).getChildren().get(0)).getText());
        if (!dir.exists() || !dir.isDirectory()) {
          packagingResultMessage.setText(
              dir.getAbsolutePath() + " is not a directory or does not exist."
          );
        }
        assetDirectories.add(dir);
      }
    } catch (ClassCastException e) {
      packagingResultMessage.setText("Internal program error.");
      return;
    }

    Author author = new Author(authorName);
    if (!authorMailTextField.getText().isBlank()) {
      author.setMail(authorMailTextField.getText());
    }
    if (!authorUrlTextField.getText().isBlank()) {
      author.setUrl(authorUrlTextField.getText());
    }
    if (!authorDonationUrlTextField.getText().isBlank()) {
      author.setDonation(authorDonationUrlTextField.getText());
    }

    Packager packager;
    try {
      packager = new Packager(outputDirectory, name, version, author);
    } catch (IOException e) {
      packagingResultMessage.setText("Error while packaging: " + e.getMessage());
      return;
    }

    if (!descriptionTextArea.getText().isBlank()) {
      packager.setDescription(descriptionTextArea.getText());
    }

    try {
      packager.setLicense(getLicense());
    } catch (ClassCastException e) {
      packagingResultMessage.setText("Internal program error.");
      return;
    }

    if (isCommercialUseAllowed.isSelected()) {
      try {
        packager.setCommercialUse(true);
        HBox box = (HBox) commercialUseMenuPane.getLeft();
        String commercialUrl = ((TextField) box.getChildren().get(1)).getText();
        if (commercialUrl.isBlank()) {
          packagingResultMessage.setText("Commercial URL is not specified!");
          return;
        }
      } catch (ClassCastException e) {
        packagingResultMessage.setText("Internal program error.");
        return;
      }
    }

    if (!tagListVBox.getChildren().isEmpty()) {
      try {
        packager.addTags(getTags());
      } catch (ClassCastException e) {
        packagingResultMessage.setText("Internal program error.");
        return;
      }
    }

    for (File spriteDirectory : assetDirectories) {
      try {
        packager.addSymbolDirectory(spriteDirectory);
      } catch (IOException e) {
        packagingResultMessage
            .setText("Copying files from " + spriteDirectory.getAbsolutePath() + " failed!");
        return;
      }
    }

    try {
      packager.reset();
    } catch (IOException e) {
      packagingResultMessage.setText("Internal program error.");
    }

    packagingResultMessage.setText("Done!");
  }

  private void addRemovableItem(TextField textField, VBox box) {
    Text tagText = new Text(textField.getText());
    tagText.setFont(Font.font(15));

    Button removeButton = new Button();
    removeButton.setText("-");
    removeButton.setPrefWidth(26);
    removeButton.setPrefHeight(26);

    HBox tagBox = new HBox();
    tagBox.setSpacing(450 - tagText.getLayoutBounds().getWidth());
    tagBox.getChildren().addAll(tagText, removeButton);

    box.getChildren().add(tagBox);

    removeButton.setOnMouseClicked(e -> box.getChildren().remove(tagBox));
    textField.setText("");
  }

  private License getLicense() {
    String option = licenseChoiceBox.getValue();
    if (option.equals("Predefined")) {
      if (License.SUPPORTED_LICENSES.isEmpty()) {
        return new License(null);
      } else {
        HBox box = (HBox) licenseMenuPane.getLeft();
        return new License((String) ((ChoiceBox<?>) box.getChildren().get(1)).getValue())
            .setCommentary(false);
      }
    }
    if (option.equals("Local file")) {
      HBox box = (HBox) licenseMenuPane.getLeft();
      return new License(((TextField) box.getChildren().get(0)).getText()).setCommentary(false)
          .setLocalFile(true);
    }
    if (option.equals("External link")) {
      HBox box = (HBox) licenseMenuPane.getLeft();
      return new License(((TextField) box.getChildren().get(1)).getText()).setCommentary(false)
          .setExternalLink(true);
    }
    return new License(null);
  }

  private List<String> getTags() {
    List<String> tags = new ArrayList<>();
    for (Node tagBoxed : tagListVBox.getChildren()) {
      tags.add(((Text) ((HBox) tagBoxed).getChildren().get(0)).getText());
    }
    return tags;
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
    // License choice setting up
    licenseChoiceBox.getItems().addAll("None", "Predefined", "Local file", "External link");
    licenseChoiceBox.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, from, to) -> changeLicenseMenu(to));
    licenseChoiceBox.setValue("None");
    changeLicenseMenu("None");

    // Commercial use setting up
    isCommercialUseAllowed.selectedProperty()
        .addListener((observableValue, from, to) -> changeCommercialMenu(to));
    changeCommercialMenu(Boolean.FALSE);

    // Tag field length limit setting up
    newTagTextField.setTextFormatter(new TextFormatter<>(change -> {
      if (change.isContentChange()) {
        int newLength = change.getControlNewText().length();
        if (newLength > 45) {
          change.setText(change.getControlNewText().substring(newLength - 45, newLength));
          change.setRange(0, change.getControlText().length());
        }
      }
      return change;
    }));
  }
}
