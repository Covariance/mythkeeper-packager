package ru.covariance.mythkeeperpackager.packager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

/**
 * Generator for support for an image set.
 *
 * @author Pavel Martynov (git: CovarianceMomentum)
 */
public class WonderdraftSymbolsPackager {

  /**
   * Creates .wonderdraft_symbols file in a provided directory with a description of an image set.
   *
   * @param dir directory to create symbols support in
   * @throws IOException if it's unable to create directory, or file provided exists and is not a *
   *                     directory, or if I/O error occurs while writing
   */
  public static void packageDirectory(File dir) throws IOException {
    Utils.safeDirectoryCreate(dir);
    File[] sprites =
        dir.listFiles(file -> (!file.isDirectory() && file.getName().endsWith(".png")));
    if (sprites == null) {
      throw new IOException("IOError while reading from " + dir.getPath());
    }

    JSONObject symbols = new JSONObject();
    for (File sprite : sprites) {
      JSONObject cur = new JSONObject();
      String name = sprite.getName().substring(0, sprite.getName().length() - 4);

      cur.put("name", name);
      cur.put("radius", 300);
      cur.put("offset_x", 0);
      cur.put("offset_y", 0);
      cur.put("draw_mode", "normal");

      symbols.put(name, cur);
    }

    File result = new File(dir.getPath() + File.pathSeparator + ".wonderdraft_symbols");
    //noinspection ResultOfMethodCallIgnored
    result.createNewFile();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(result, false))) {
      writer.write(symbols.toString());
    }
  }
}
