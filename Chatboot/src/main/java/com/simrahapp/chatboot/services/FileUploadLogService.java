package com.simrahapp.chatboot.services;

import com.simrahapp.chatboot.entity.FileUploadLog;
import com.simrahapp.chatboot.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class FileUploadLogService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    public void logFileUpload(UUID messageId, String fileType,
                              int charCount) {
        FileUploadLog log = new FileUploadLog();
        log.setMessageId(messageId);
        log.setFileType(fileType);
        log.setNoOfFilesUploaded(1);
        log.setNoOfReadableCharacters(charCount);
        fileUploadRepository.save(log);
    }
}