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
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private FileRepository fileRepository;


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

    public FileDTO getFileByTableAndId(String tableName, String tableId) {
        Optional<FileEntity> optional = fileRepository.findByTableNameAndTableIdAndDeleteAt(tableName, tableId, "N");
        return optional.map(FileDTO::toFileDto).orElse(null);
    }
}
