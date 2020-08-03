package ru.covariance.mythkeeperPackager;

import org.json.JSONObject;

/**
 * @author Pavel Martynov (git: CovarianceMomentum)
 */
public class License {
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
