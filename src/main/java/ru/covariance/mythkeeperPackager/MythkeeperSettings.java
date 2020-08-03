package ru.covariance.mythkeeperPackager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

/**
 * @author Pavel Martynov (git: CovarianceMomentum)
 */

public class MythkeeperSettings {
    public static void createMythkeeperSettings(File dir, String name, String version,
                                                Author author, Author distributor,
                                                boolean commercialUse, String commercialURL,
                                                License license, List<String> tags,
                                                String description) throws IOException {
        if (!dir.exists()) {
            throw new FileNotFoundException("File " + dir.getPath() + " does not exist");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("File " + dir.getPath() + " is not a directory");
        }
        File settings = new File(dir.getPath() + "\\mythKeeperSettings.json");
        //noinspection ResultOfMethodCallIgnored
        settings.createNewFile();

        JSONObject wholeSettings = new JSONObject(), inner = new JSONObject();

        inner.put("name", name);
        inner.put("version", version);
        inner.put("author", author.getRepresentation());
        inner.put("distributor", distributor.getRepresentation());
        inner.put("commercialUse", commercialUse);
        inner.put("commercialURL", commercialURL != null ? commercialURL : false);
        inner.put("license", license.getRepresentation());
        inner.put("tags", new JSONArray(tags));
        inner.put("description", description);

        wholeSettings.put("basicInformation", inner);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(settings, false))) {
            writer.write(wholeSettings.toString());
        }
    }
}
