package com.simrahapp.chatboot.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "image_uploading")
public class ImageUploadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "img_id")
    private UUID imgId;

    @Column(name = "message_id")
    private UUID messageId;

    @Column(name = "no_of_images_uploaded")
    private Integer noOfImagesUploaded;

    @Column(name = "image_size")
    private String imageSize;

    public UUID getImgId() { return imgId; }
    public void setImgId(UUID imgId) { this.imgId = imgId; }
    public UUID getMessageId() { return messageId; }
    public void setMessageId(UUID messageId) { this.messageId = messageId; }
    public Integer getNoOfImagesUploaded() { return noOfImagesUploaded; }
    public void setNoOfImagesUploaded(Integer n) { this.noOfImagesUploaded = n; }
    public String getImageSize() { return imageSize; }
    public void setImageSize(String imageSize) { this.imageSize = imageSize; }
}