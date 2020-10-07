package ru.covariance.mythkeeperpackager.packager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Generator for Mythkeeper settings.
 *
 * @author Pavel Martynov (git: CovarianceMomentum)
 */
public class MythkeeperSettings {

  /**
   * Creates mythKeeperSettings.json file in a provided directory with a description of a package.
   *
   * @param dir           directory to create settings file in
   * @param name          name of the package
   * @param version       version of the package
   * @param author        author of the package
   * @param distributor   distributor of the package
   * @param commercialUse is commercial use allowed for the package
   * @param commercialUrl commercial URL for the package if paid version of items exists (null if it
   *                      is absent)
   * @param license       license of the package
   * @param tags          list of tags of the package
   * @param description   description of the package
   * @throws IOException if it's unable to create directory, or file provided exists and is not a
   *                     directory, or if I/O error occurs while writing
   */
  public static void createMythkeeperSettings(File dir, String name, String version, Author author,
      Author distributor, boolean commercialUse,
      String commercialUrl, License license,
      List<String> tags,
      String description) throws IOException {
    Utils.safeDirectoryCreate(dir);
    File settings = new File(dir.getPath() + File.separator + "mythKeeperSettings.json");
    //noinspection ResultOfMethodCallIgnored
    settings.createNewFile();

    JSONObject inner = new JSONObject();

    inner.put("name", name);
    inner.put("version", version);
    inner.put("author", author.getRepresentation());
    inner.put("distributor", distributor.getRepresentation());
    inner.put("commercialUse", commercialUse);
    inner.put("commercialURL", commercialUrl != null ? commercialUrl : false);
    inner.put("license", license.getRepresentation());
    inner.put("tags", new JSONArray(tags));
    inner.put("description", description);

    JSONObject wholeSettings = new JSONObject();
    wholeSettings.put("basicInformation", inner);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(settings, false))) {
      writer.write(wholeSettings.toString());
    }
  }
}
