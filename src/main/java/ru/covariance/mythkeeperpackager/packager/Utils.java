package ru.covariance.mythkeeperpackager.packager;

import java.io.File;
import java.io.IOException;

class Utils {

  static void safeDirectoryCreate(File dir) throws IOException {
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        throw new IOException("Unable to create output directory");
      }
    }

    if (!dir.isDirectory()) {
      throw new IllegalArgumentException("Path does not specify a directory");
    }
  }
}
