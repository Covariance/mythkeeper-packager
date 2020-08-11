package ru.covariance.mythkeeperpackager.packager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 * Object that is packaging assets with a Mythkeeper settings and metafiles.
 *
 * @author Pavel Martynov (git: CovarianceMomentum)
 */
public class Packager {

  private static final Author DISTRIBUTOR =
      new Author("CovarianceMomentum").setMail("covariancemomentum@gmail.com")
          .setUrl("https://github.com/CovarianceMomentum")
          .setDonation("https://github.com/CovarianceMomentum");

  final File outputDirectory;
  String name;
  String version;
  Author author;

  String description;
  License license = new License(null);
  boolean commercialUse = false;
  String commercialUrl;
  List<String> tags = new ArrayList<>();

  private File derivedDirectory(String derive) {
    return new File(outputDirectory.getPath() + File.pathSeparator + derive);
  }

  /**
   * Creates new packager with a provided output directory and a default Distributor.
   *
   * @param outputDirectory directory to create your asset package in
   * @param name            name of your asset package
   * @param version         version of your asset package
   * @param author          author of your
   * @throws IOException if it's unable to create output directory or if it's not a directory
   */
  public Packager(final File outputDirectory, String name, String version,
      Author author) throws IOException {
    this.outputDirectory = outputDirectory;
    this.name = name;
    this.version = version;
    this.author = author;

    Utils.safeDirectoryCreate(outputDirectory);
    Utils.safeDirectoryCreate(derivedDirectory("metafiles" + File.pathSeparator + "gallery"));

    this.reset();
  }

  public Packager setDescription(String description) {
    this.description = description;
    return this;
  }

  public Packager setLicense(License license) {
    this.license = license;
    return this;
  }

  public Packager setCommercialUse(boolean commercialUse) {
    this.commercialUse = commercialUse;
    return this;
  }

  public Packager setCommercialUrl(String commercialUrl) {
    this.commercialUrl = commercialUrl;
    return this;
  }

  public Packager setTag(List<String> tags) {
    this.tags = tags;
    return this;
  }

  /**
   * Adds provided tag to the tag list.
   *
   * @param tag tag to add to the list
   * @return this object
   */
  public Packager addTag(String tag) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tag);
    return this;
  }

  /**
   * Adds provided tags to the tag list.
   *
   * @param tags tags to add to the list
   * @return this object
   */
  public Packager addTags(List<String> tags) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.addAll(tags);
    return this;
  }

  /**
   * Recreates mythkeeper settings.
   *
   * @return this object
   * @throws IOException if I/O error occurs while writing
   */
  public Packager reset() throws IOException {
    MythkeeperSettings.createMythkeeperSettings(outputDirectory, name, version, author, DISTRIBUTOR,
        commercialUse, commercialUrl, license, tags,
        description);
    return this;
  }

  /**
   * Creates symbols directory in a current packager output directory.
   *
   * @param inputDir directory with .png images to pack, MUST contain at least one image
   * @return this object
   * @throws IOException if output directory cannot be created
   */
  public Packager addSymbolDirectory(File inputDir) throws IOException {
    if (!inputDir.exists() || !inputDir.isDirectory()) {
      throw new IllegalArgumentException("path does not specify a directory");
    }

    File outputDir = derivedDirectory(
        "sprites" + File.pathSeparator + "symbols" + File.pathSeparator + inputDir.getName());

    Utils.safeDirectoryCreate(outputDir);

    File[] images =
        inputDir.listFiles(file -> (!file.isDirectory() && file.getName().endsWith(".png")));

    if (images == null) {
      throw new IllegalArgumentException("directory specified by path does not contain any images");
    }

    for (File image : images) {
      FileUtils.copyFile(image, new File(outputDir + File.pathSeparator + image.getName()));
    }

    WonderdraftSymbolsPackager.packageDirectory(outputDir);

    return this;
  }
}
