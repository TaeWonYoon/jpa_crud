package com.study.file.service;

import com.study.board.service.BoardService;
import com.study.file.entity.FileEntity;
import com.study.file.model.FileDTO;
import com.study.file.repository.FileRepository;
import com.study.board.entity.BoardEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private FileRepository fileRepository;

    //테이블 저장
    public void saveFile(MultipartFile file, BoardEntity board) throws IOException {
        log.info("########### FileService saveFile() start ###########");

        String uploadDir = "C:/uploads/board/";

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String storedFilename = UUID.randomUUID().toString() + extension;
        File saveFile = new File(uploadPath, storedFilename);

        file.transferTo(saveFile); //파일 업로드

        // 파일 엔티티 저장
        FileEntity fileEntity = new FileEntity();
        fileEntity.setTableName("board");
        fileEntity.setTableId(board.getId().toString());
        fileEntity.setFileName(storedFilename);
        fileEntity.setFileNameOrigin(originalFilename);
        fileEntity.setFileExt(extension);
        fileEntity.setFileSize(String.valueOf(file.getSize()));
        fileEntity.setFilePath(saveFile.getAbsolutePath());
        // userId는 상황에 맞게 넣으세요 (예: 로그인 유저 아이디)
        fileEntity.setUserId("defaultUser");

        log.info("fileEntity={}", fileEntity);
        fileRepository.save(fileEntity);
    }

    //테이블 조회 where table, id
    public FileDTO getFileByTableAndId(String tableName, String tableId) {
        log.info("########### FileService getFileByTableAndId() start ###########");

        Optional<FileEntity> optional = fileRepository.findByTableNameAndTableIdAndDeleteAt(tableName, tableId, "N");
        return optional.map(FileDTO::toFileDto).orElse(null);
    }

    //파일 조회
    public FileDTO getFileById(Long id) {
        log.info("########### FileService getFileById() start ###########");

        Optional<FileEntity> optional = fileRepository.findById(id);
        return optional.map(FileDTO::toFileDto).orElse(null);
    }

    //첨부 파일 삭제
    public boolean deleteFile(Long fileId) {
        log.info("########### FileService deleteFile() start ###########");
        Optional<FileEntity> optionalFile = fileRepository.findById(fileId);

        if (optionalFile.isEmpty()) return false;

        FileEntity file = optionalFile.get();

        // 실제 파일 경로
        Path filePath = Paths.get(file.getFilePath(), file.getFileName());

        try {
            // 실제 파일 삭제
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 삭제 실패");
        }

        // 논리 삭제 처리
        file.setDeleteAt("Y");
        fileRepository.save(file);

        return true;
    }

    //수정 시 파일 업로드(추가,삭제)
    public int fileEditUpload(Long id, MultipartFile file) {
        log.info("########### FileService fileEditUpload() start ###########");

        if (file != null && !file.isEmpty()) {

            // 1. 기존 파일 존재 시 삭제 처리
            fileRepository.findByTableNameAndTableIdAndDeleteAt("board", String.valueOf(id), "N")
                    .ifPresent(oldFile -> {
                        Path oldPath = Paths.get(oldFile.getFilePath(), oldFile.getFileName());
                        try {
                            Files.deleteIfExists(oldPath);
                        } catch (IOException e) {
                            log.warn("기존 파일 삭제 실패: {}", oldPath, e);
                        }
                        oldFile.setDeleteAt("Y");
                        fileRepository.save(oldFile);
                    });

            // 2. 파일 정보 준비
            String fileNameOrigin = file.getOriginalFilename();
            String fileExt = "";
            if (fileNameOrigin != null && fileNameOrigin.contains(".")) {
                fileExt = fileNameOrigin.substring(fileNameOrigin.lastIndexOf(".")).toLowerCase();
            }

            // 3. 저장할 이름 및 경로
            String storedFilename = UUID.randomUUID().toString() + fileExt;
            String uploadDir = "C:/uploads/board/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            File saveFile = new File(uploadPath, storedFilename);

            try {
                file.transferTo(saveFile);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e);
            }

            // 4. DB 저장
            FileEntity fileEntity = new FileEntity();
            fileEntity.setTableName("board");
            fileEntity.setTableId(String.valueOf(id));
            fileEntity.setFileName(storedFilename); // 실제 저장된 파일 이름
            fileEntity.setFileNameOrigin(fileNameOrigin);
            fileEntity.setFileExt(fileExt);
            fileEntity.setFilePath(uploadDir);
            fileEntity.setFileSize(String.valueOf(file.getSize()));
            fileEntity.setUserId("defaultUser"); // 이후 세션 값으로 대체
            fileRepository.save(fileEntity);
        }

        return 1;
    }
}
