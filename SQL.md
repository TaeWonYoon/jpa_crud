-- jpa_crud.board definition

CREATE TABLE `board` (
`id` bigint NOT NULL AUTO_INCREMENT,
`title` varchar(45) NOT NULL COMMENT '제목',
`content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '내용',
`delete_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'N',
`created_id` bigint DEFAULT NULL,
`created_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '등록자',
`created_date` timestamp NULL DEFAULT NULL COMMENT '작성일',
`updated_id` bigint DEFAULT NULL,
`updated_date` timestamp NULL DEFAULT NULL COMMENT '수정일',
`views` int DEFAULT '0' COMMENT '조회수',
`regist_ty` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '게시글등록구분',
PRIMARY KEY (`id`),
KEY `FKkmvl8ky45u3brglny2hmpy3ur` (`created_id`),
CONSTRAINT `FKkmvl8ky45u3brglny2hmpy3ur` FOREIGN KEY (`created_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- jpa_crud.files definition

CREATE TABLE `files` (
`id` bigint NOT NULL AUTO_INCREMENT,
`table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`table_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`file_name_origin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`file_ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`file_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
`created_at` timestamp NULL DEFAULT NULL,
`updated_at` timestamp NULL DEFAULT NULL,
`delete_at` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'N',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- jpa_crud.`user` definition

CREATE TABLE `user` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'pk',
`login_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '로그인아이디',
`password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '비밀번호',
`name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '이름',
`level` int NOT NULL DEFAULT '1' COMMENT '권한(1사용자,9관리자)',
`created_date` timestamp NULL DEFAULT NULL COMMENT '등록일',
`updated_date` timestamp NULL DEFAULT NULL COMMENT '수정일',
`delete_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'N' COMMENT '정상 N 삭제 Y',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- jpa_crud.introduction definition

CREATE TABLE `introduction` (
`id` bigint NOT NULL AUTO_INCREMENT,
`title` varchar(45) NOT NULL,
`content` varchar(1000) NOT NULL,
`created_id` bigint DEFAULT NULL COMMENT '작성자',
`created_name` varchar(255) DEFAULT NULL,
`created_date` timestamp NULL DEFAULT NULL COMMENT '작성일',
`updated_id` bigint DEFAULT NULL COMMENT '수정자',
`updated_name` varchar(255) DEFAULT NULL,
`updated_date` timestamp NULL DEFAULT NULL COMMENT '수정일',
`delete_at` varchar(255) DEFAULT NULL,
`sort` int DEFAULT NULL COMMENT '정렬',
`use_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '사용여부',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='소개_테이블';


