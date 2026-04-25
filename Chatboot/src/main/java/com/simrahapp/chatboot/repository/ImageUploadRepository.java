package com.simrahapp.chatboot.repository;

import com.simrahapp.chatboot.entity.ImageUploadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ImageUploadRepository
        extends JpaRepository<ImageUploadLog, UUID> {}