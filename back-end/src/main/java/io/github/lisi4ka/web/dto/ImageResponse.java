package io.github.lisi4ka.web.dto;

import java.time.LocalDateTime;

public class ImageResponse {
    private Long id;
    private String title;
    private String description;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private LocalDateTime uploadDate;

    // Конструкторы, геттеры и сеттеры
    public ImageResponse() {}

    public ImageResponse(Long id, String title, String description, String fileName,
                         String originalFileName, String fileType, Long fileSize,
                         LocalDateTime uploadDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
    }

    // Геттеры и сеттеры...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}