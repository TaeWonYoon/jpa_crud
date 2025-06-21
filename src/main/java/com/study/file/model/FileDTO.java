package com.study.file.model;

import com.study.file.entity.FileEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDTO {
    private Long id;
    private String tableName;
    private String tableId;
    private String fileName;
    private String fileNameOrigin;
    private String fileExt;
    private String fileSize;
    private String filePath;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deleteAt;

    // 기본 생성자, getter/setter 등 생략

    // 엔티티 -> DTO 변환 메서드
    public static FileDTO toFileDto(FileEntity entity) {
        FileDTO dto = new FileDTO();
        dto.setId(entity.getId());
        dto.setTableName(entity.getTableName());
        dto.setTableId(entity.getTableId());
        dto.setFileName(entity.getFileName());
        dto.setFileNameOrigin(entity.getFileNameOrigin());
        dto.setFileExt(entity.getFileExt());
        dto.setFileSize(entity.getFileSize());
        dto.setFilePath(entity.getFilePath());
        dto.setUserId(entity.getUserId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setDeleteAt(entity.getDeleteAt());
        return dto;
    }
}