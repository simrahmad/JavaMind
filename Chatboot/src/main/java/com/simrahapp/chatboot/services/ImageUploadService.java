package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.ImageUploadLog;
import com.simrahapp.chatboot.repository.ImageUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Autowired
    private ImageUploadRepository imageUploadRepository;

    public void logImageUpload(UUID messageId, long fileSizeBytes) {
        ImageUploadLog log = new ImageUploadLog();
        log.setMessageId(messageId);
        log.setNoOfImagesUploaded(1);
        log.setImageSize(fileSizeBytes / 1024 + " KB");
        imageUploadRepository.save(log);
    }
}