package ru.covariance.mythkeeperPackager;

import org.json.JSONObject;

import java.io.*;

/**
 * @author Pavel Martynov (git: CovarianceMomentum)
 */

public class WonderdraftSymbolsPackager {
    public static void packageDirectory(File dir) throws IOException {
        if (!dir.exists()) {
            throw new FileNotFoundException("File " + dir.getPath() + " does not exist");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("File " + dir.getPath() + " is not a directory");
        }
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

        File result = new File(dir.getPath() + "\\.wonderdraft_symbols");
        //noinspection ResultOfMethodCallIgnored
        result.createNewFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(result, false))) {
            writer.write(symbols.toString());
        }
    }
}
