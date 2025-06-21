package com.study.file.repository;

import com.study.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByTableNameAndTableIdAndDeleteAt(String tableName, String tableId, String deleteAt);
}
