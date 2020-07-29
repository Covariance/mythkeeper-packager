package ru.covariance.mythkeeperPackager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

/**
 * @author Pavel Martynov (git: CovarianceMomentum)
 */

public class MythkeeperSettings {
    public static class Author {
        String name;
        boolean mailExists = false;
        String mail;
        boolean urlExists = false;
        String url;
        boolean donationExists = false;
        String donation;

        public Author(String name) {
            this.name = name;
        }

        public Author setMail(String mail) {
            if (mail == null) {
                this.mailExists = false;
            } else {
                this.mailExists = true;
                this.mail = mail;
            }
            return this;
        }

        public Author setUrl(String url) {
            if (url == null) {
                this.urlExists = false;
            } else {
                this.urlExists = true;
                this.url = url;
            }
            return this;
        }

        public Author setDonation(String donation) {
            if (donation == null) {
                this.donationExists = false;
            } else {
                this.donationExists = true;
                this.donation = donation;
            }
            return this;
        }

        public JSONObject getRepresentation() {
            JSONObject result = new JSONObject();

            result.put("exists", name != null);

            if (name == null) {
                return result;
            }

            result.put("name", name);
            result.put("mail", mailExists ? mail : false);
            result.put("url", urlExists ? url : false);
            result.put("donation", donationExists ? donation : false);

            return result;
        }
    }

    public static class License {
        String license;
        boolean localFile;
        boolean externalLink;
        boolean commentary;

        public License(String license) {
            this.license = license;
        }

        public License setLocalFile(boolean localFile) {
            this.localFile = localFile;
            return this;
        }

        public License setExternalLink(boolean externalLink) {
            this.externalLink = externalLink;
            return this;
        }

        public License setCommentary(boolean commentary) {
            this.commentary = commentary;
            return this;
        }

        public JSONObject getRepresentation() throws IllegalStateException {
            JSONObject result = new JSONObject();

            result.put("hasLicense", license != null);

            if (license == null) {
                return result;
            }

            if (localFile && externalLink) {
                throw new IllegalStateException(
                        "LocalFile and ExternalLink cannot be set true at the same time");
            }

            if (!license.equals("CC BY 4.0") && !license.equals("CC BY-SA 4.0") &&
                !license.equals("CC BY-ND 4.0") && !license.equals("CC BY-NC 4.0") &&
                !license.equals("CC BY-NC-SA 4.0") && !license.equals("CC BY-NC-ND 4.0") &&
                !localFile && !externalLink) {
                throw new IllegalStateException("License set to true but no supported license, " +
                                                "local file or external link provided");
            }

            result.put("type", license);
            result.put("localFile", localFile);
            result.put("externalLink", externalLink);
            result.put("commentary", commentary);

            return result;
        }
    }

    public static void createMythkeeperSettings(File dir, String name, String version,
                                                Author author, Author distributor,
                                                boolean commercialUse, boolean commercialURLExists,
                                                String commercialURL, License license,
                                                List<String> tags,
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
        inner.put("commercialURL", commercialURLExists ? commercialURL : false);
        inner.put("license", license.getRepresentation());
        inner.put("tags", new JSONArray(tags));
        inner.put("description", description);

        wholeSettings.put("basicInformation", inner);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(settings, false))) {
            writer.write(wholeSettings.toString());
        }
    }
}
