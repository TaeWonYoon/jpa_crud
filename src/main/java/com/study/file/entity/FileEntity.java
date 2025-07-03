package com.study.file.entity;

import com.study.admin.introduction.entity.IntroductionEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name")
    private String tableName;  // 예: "board"

    @Column(name = "file_name")
    private String fileName; // 저장된 파일명

    @Column(name = "file_name_origin")
    private String fileNameOrigin; // 원본 파일명

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "delete_at", length = 1)
    private String deleteAt = "N";

    @Column(name = "table_id")
    private Long tableId;  // PK와 맞추기

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        deleteAt = "N";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // getters/setters
}
