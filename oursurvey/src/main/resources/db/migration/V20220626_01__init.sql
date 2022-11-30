CREATE TABLE IF NOT EXISTS `pk_manager` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `table_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `prefix` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `yy` int NOT NULL,
    `mm` int NOT NULL,
    `dd` int NOT NULL,
    `alpha` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'A',
    `count` int NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `grade` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `pivot` int NOT NULL,
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `grade_id` bigint NOT NULL,
    `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `nickname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `gender` enum('M','F','E') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `age` int DEFAULT NULL,
    `tel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`),
    KEY `FK_user_grade` (`grade_id`),
    CONSTRAINT `FK_user_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `file` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `table_pk` bigint DEFAULT NULL,
    `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `origin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `dir` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `ext` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_file_user` (`user_id`),
    CONSTRAINT `FK_file_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `logged_in` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `remote_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_logged_in_user` (`user_id`),
    CONSTRAINT `FK_logged_in_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS `point` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `value` int NOT NULL,
    `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `table_pk` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
    `table_name` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_point_user` (`user_id`),
    CONSTRAINT `FK_point_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `experience` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `value` int NOT NULL,
    `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `table_pk` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
    `table_name` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_experience_user` (`user_id`),
    CONSTRAINT `FK_experience_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `survey` (
    `id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `user_id` bigint NOT NULL,
    `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `content` text COLLATE utf8mb4_general_ci NOT NULL,
    `minute` int NOT NULL,
    `start_date` date NOT NULL,
    `end_date` date NOT NULL,
    `open_fl` tinyint NOT NULL,
    `temp_fl` tinyint NOT NULL,
    `closing_comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `pull_date` datetime DEFAULT NULL,
    `view_cnt` BIGINT(19) NULL DEFAULT '0',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_survey_user` (`user_id`),
    CONSTRAINT `FK_survey_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `hashtag` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `value` (`value`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `hashtag_survey` (
    `survey_id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `hashtag_id` bigint NOT NULL,
    UNIQUE KEY `survey_id_hashtag_id` (`survey_id`,`hashtag_id`),
    KEY `FK_hashtag_survey_hashtag` (`hashtag_id`),
    KEY `FK_hashtag_survey_survey` (`survey_id`) USING BTREE,
    CONSTRAINT `FK_hashtag_survey_hashtag` FOREIGN KEY (`hashtag_id`) REFERENCES `hashtag` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_hashtag_survey_survey` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `section` (
    `id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `survey_id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `content` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `next_section` bigint NOT NULL DEFAULT '0',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_section_survey` (`survey_id`),
    CONSTRAINT `FK_section_survey` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `question` (
    `id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `section_id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `ask` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `descrip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
    `oder` int NOT NULL,
    `multi_fl` tinyint NOT NULL,
    `dupl_fl` tinyint NOT NULL,
    `ess_fl` tinyint NOT NULL,
    `random_show_fl` tinyint NOT NULL,
    `next_fl` tinyint NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_question_section` (`section_id`),
    CONSTRAINT `FK_question_section` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `question_item` (
    `id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `question_id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `oder` int NOT NULL,
    `next_section` bigint DEFAULT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_question_item_question` (`question_id`),
    CONSTRAINT `FK_question_item_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `reply` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint DEFAULT NULL,
    `survey_id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_reply_user` (`user_id`),
    KEY `FK_reply_survey` (`survey_id`),
    CONSTRAINT `FK_reply_survey` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_reply_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `answer` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `reply_id` bigint NOT NULL,
    `question_id` varchar(14) COLLATE utf8mb4_general_ci NOT NULL,
    `response` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_answer_reply` (`reply_id`),
    KEY `FK_answer_question` (`question_id`),
    CONSTRAINT `FK_answer_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_answer_reply` FOREIGN KEY (`reply_id`) REFERENCES `reply` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;