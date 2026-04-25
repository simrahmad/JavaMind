package com.simrahapp.chatboot.repository;

import com.simrahapp.chatboot.entity.FileUploadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadLog, UUID> {}