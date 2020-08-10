package ru.covariance.mythkeeperPackager.packager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Martynov (git: CovarianceMomentum)
 */

public class Packager {
    private final static Author DISTRIBUTOR =
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
    String commercialURL;
    List<String> tags = new ArrayList<>();

    private void safeDirectoryCreate(File dir) throws FileSystemException {
        if (!dir.exists()) {
            if (!outputDirectory.mkdirs()) {
                throw new FileSystemException("Unable to create output directory");
            }
        }

        if (!outputDirectory.isDirectory()) {
            throw new IllegalArgumentException("Output path does not specify a directory");
        }
    }

    private File derivedDirectory(String derive) {
        return new File(outputDirectory.getPath() + File.pathSeparator + derive);
    }

    public Packager(final File outputDirectory, String name, String version,
                    Author author) throws IOException {
        this.outputDirectory = outputDirectory;
        this.name = name;
        this.version = version;
        this.author = author;

        safeDirectoryCreate(outputDirectory);
        safeDirectoryCreate(derivedDirectory("metafiles" + File.pathSeparator + "gallery"));

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

    public Packager setCommercialURL(String commercialURL) {
        this.commercialURL = commercialURL;
        return this;
    }

    public Packager setTag(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public Packager addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
        return this;
    }

    public Packager reset() throws IOException {
        MythkeeperSettings
                .createMythkeeperSettings(outputDirectory, name, version, author, DISTRIBUTOR,
                                          commercialUse, commercialURL, license, tags, description);
        return this;
    }

    public Packager addSymbolDirectory(File inputDir) throws IOException {
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            throw new IllegalArgumentException("path does not specify a directory");
        }

        File outputDir = derivedDirectory(
                "sprites" + File.pathSeparator + "symbols" + File.pathSeparator +
                inputDir.getName());

        safeDirectoryCreate(outputDir);

        File[] images = inputDir.listFiles(
                file -> (!file.isDirectory() && file.getName().endsWith(".png")));

        if (images == null) {
            throw new IllegalArgumentException(
                    "directory specified by path does not contain any images");
        }

        for (File image : images) {
            FileUtils.copyFile(image, new File(outputDir + File.pathSeparator + image.getName()));
        }

        WonderdraftSymbolsPackager.packageDirectory(outputDir);

        return this;
    }
}
