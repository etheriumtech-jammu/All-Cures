package service;

/**
 * Holds metadata about a saved file — where it’s stored and how.
 */
public class FileStorageResult {

    private final String path;          // local path or S3 URL
    private final String storageType;   // e.g. "LOCAL" or "S3"

    public FileStorageResult(String path, String storageType) {
        this.path = path;
        this.storageType = storageType;
    }

    public String getPath() {
        return path;
    }

    public String getStorageType() {
        return storageType;
    }
}
