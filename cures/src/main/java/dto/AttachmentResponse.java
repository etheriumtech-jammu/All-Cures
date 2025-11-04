package dto;

public class AttachmentResponse {
    private Integer presId;
    private String filePath;
    private String uploadedAt;

    public AttachmentResponse(Integer presId, String filePath, String uploadedAt) {
        this.presId = presId;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public Integer getPresId() { return presId; }
    public String getFilePath() { return filePath; }
    public String getUploadedAt() { return uploadedAt; }
}
