package com.simrahapp.chatboot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "files_uploading")
public class FileUploadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "file_id")
    private UUID fileId;

    @Column(name = "message_id")
    private UUID messageId;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "no_of_files_uploaded")
    private Integer noOfFilesUploaded;

    @Column(name = "no_of_readable_characters")
    private Integer noOfReadableCharacters;

    // Getters and Setters
    public UUID getFileId() { return fileId; }
    public void setFileId(UUID fileId) { this.fileId = fileId; }

    public UUID getMessageId() { return messageId; }
    public void setMessageId(UUID messageId) { this.messageId = messageId; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Integer getNoOfFilesUploaded() { return noOfFilesUploaded; }
    public void setNoOfFilesUploaded(Integer n) { this.noOfFilesUploaded = n; }

    public Integer getNoOfReadableCharacters() { return noOfReadableCharacters; }
    public void setNoOfReadableCharacters(Integer n) { this.noOfReadableCharacters = n; }
}