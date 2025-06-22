package com.study.file.controller;

import com.study.file.model.FileDTO;
import com.study.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {
        FileDTO fileDTO = fileService.getFileById(fileId);
        if (fileDTO == null) {
            return ResponseEntity.notFound().build();
        }

        // 파일 저장 경로 + 저장된 파일명
        Path filePath = Paths.get(fileDTO.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = URLEncoder.encode(fileDTO.getFileNameOrigin(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        boolean deleted = fileService.deleteFile(fileId);
        if (deleted) {
            return ResponseEntity.ok().body("삭제 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일 없음");
        }
    }
}
