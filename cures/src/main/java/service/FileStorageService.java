package service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction for storing uploaded prescription files (image/pdf).
 * This can have multiple implementations — local, S3, etc.
 */
public interface FileStorageService {

    /**
     * Saves the uploaded file and returns its storage result (path/URL and storage type).
     */
    FileStorageResult save(MultipartFile file);

    /**
     * Deletes the given file from storage (local or remote).
     */
    void delete(String path);
}
