package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Local file storage implementation — saves files to the server filesystem.
 * Base path is configurable via 'app.storage.local.base-path' in application.properties.
 */
@Service
@Profile("!s3") // default unless you're using an S3 profile
public class LocalFileStorageService implements FileStorageService {

   
 //   private String basePath="C:\\Users\\Divya\\Documents\\Desktop"; // default ./uploads if not specified

	private String basePath="/home/uat/Production/installers/tomcat/webapps/cures_articleimages/prescription";
    @Override
    public FileStorageResult save(MultipartFile file) {
        try {
            // Create date-based folder: e.g., /uploads/2025-10-22
            Path dateDir = Paths.get(basePath, LocalDate.now().toString());
            Files.createDirectories(dateDir);

            // Build a unique filename: timestamp + UUID + sanitized original filename
            String cleanName = sanitize(file.getOriginalFilename());
            String uniqueName = System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + cleanName;

            Path targetPath = dateDir.resolve(uniqueName);

            // Save file
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Return the absolute path (could be converted to public URL if served via static endpoint)
            String absolutePath = targetPath.toAbsolutePath().toString();
            return new FileStorageResult(absolutePath, "SERVER");

        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            // You can log this but don’t rethrow (cleanup should not block)
            System.err.println("Failed to delete file: " + path + " due to " + e.getMessage());
        }
    }

    /**
     * Replaces unsafe characters in filenames to prevent path traversal and encoding issues.
     */
    private String sanitize(String name) {
        if (name == null || name.isBlank()) return "file";
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
