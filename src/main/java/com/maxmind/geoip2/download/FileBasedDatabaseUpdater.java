/* HEADER */
package com.maxmind.geoip2.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Concrete implementation of {@link DatabaseUpdater} that updates {@link File}s.
 */
public class FileBasedDatabaseUpdater {
    private static final String DATABASE_FILE_EXTENSION = ".mmdb";
    private static final String CHECKSUM_FILE_EXTENSION = ".checksum";

    private final DatabaseDownloader downloader;
    private final File database;
    private String databaseMd5;

    public FileBasedDatabaseUpdater(DatabaseDownloader downloader, File database) throws IOException {
        this.downloader = downloader;
        this.database = database;
        databaseMd5 = readCheckSum();
    }

    private String readCheckSum() throws IOException {
        Path checkSumPath = getCheckSumPath();
        if (checkSumPath.toFile().exists()) {
            byte[] readAllBytes = Files.readAllBytes(checkSumPath);
            return new String(readAllBytes, "UTF-8");
        }
        return null;
    }

    private Path getCheckSumPath() {
        return new File(database.getParent(), database.getName() + CHECKSUM_FILE_EXTENSION).toPath();
    }

    /**
     * Verifies the current available version and if a newer one exists, downloads it replacing the current version.
     *
     * @return
     * @throws IOException
     */
    public boolean refresh() throws IOException {
        String databaseMd5 = downloader.getDatabaseMd5();
        if (this.databaseMd5 == null || !this.databaseMd5.equals(databaseMd5)) {
            replaceExistingDatabase();
            writeCheckSum(databaseMd5);
            this.databaseMd5 = databaseMd5;
            return true;
        }
        return false;
    }

    private void replaceExistingDatabase() throws IOException {
        File tmpTarFile = Files.createTempFile(database.getName() + "-", ".tar.gz").toFile();
        downloader.downloadDatabaseToFile(tmpTarFile);

        File replacementDatabase = new File(tmpTarFile.getParentFile(), database.getName() + DATABASE_FILE_EXTENSION);
        extractAndOverwriteFile(replacementDatabase, tmpTarFile);
        tmpTarFile.delete();

        CopyOption[] copyOptions = { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES };
        Files.copy(replacementDatabase.toPath(), database.toPath(), copyOptions);
        replacementDatabase.delete();
    }

    private static void extractAndOverwriteFile(File database, File tmpTarFile) throws IOException {
        ArchiveInputStream tarInputStream = new TarArchiveInputStream(
                        new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(tmpTarFile))));
        try {
            ArchiveEntry entry = null;
            while ((entry = tarInputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(DATABASE_FILE_EXTENSION)) {
                    final OutputStream outputFileStream = new BufferedOutputStream(new FileOutputStream(database));
                    IOUtils.copy(tarInputStream, outputFileStream);
                    IOUtils.closeQuietly(outputFileStream);
                    break;
                }
            }
        } finally {
            IOUtils.closeQuietly(tarInputStream);
        }
    }

    private void writeCheckSum(String databaseMd5) throws IOException, UnsupportedEncodingException {
        Path checkSumPath = getCheckSumPath();
        Files.write(checkSumPath, databaseMd5.getBytes("UTF-8"));
    }
}