DROP TABLE IF EXISTS `logged_in`;
DROP TABLE IF EXISTS `file`;
DROP TABLE IF EXISTS `top_survey`;
DROP TABLE IF EXISTS `experience`;
DROP TABLE IF EXISTS `hashtag_survey`;
DROP TABLE IF EXISTS `hashtag`;
DROP TABLE IF EXISTS `point`;
DROP TABLE IF EXISTS `answer`;
DROP TABLE IF EXISTS `reply`;
DROP TABLE IF EXISTS `question_item`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `section`;
DROP TABLE IF EXISTS `survey`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `grade`;

CREATE TABLE IF NOT EXISTS `file` (
    `id` int NOT NULL AUTO_INCREMENT,
    `table_pk` bigint NOT NULL,
    `table_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `origin_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `path` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `ext` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `grade` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `pivot` int NOT NULL,
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `grade_id` bigint NOT NULL,
    `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `pwd` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `gender` enum('M','F') COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `age` date NULL default NULL,
    `tel` varchar(50) COLLATE utf8mb4_general_ci NULL default NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email` (`email`),
    KEY `FK_user_grade` (`grade_id`),
    CONSTRAINT `FK_user_grade` FOREIGN KEY (`grade_id`) REFERENCES `grade` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `logged_in` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `remote_addr` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_logged_in_user` (`user_id`),
    CONSTRAINT `FK_logged_in_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `point` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `value` int NOT NULL,
    `reason` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `table_pk` bigint DEFAULT '0',
    `table_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_point_user` (`user_id`),
    CONSTRAINT `FK_point_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `experience` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `value` int NOT NULL,
    `reason` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `table_pk` bigint DEFAULT '0',
    `table_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_experience_user` (`user_id`),
    CONSTRAINT `FK_experience_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `survey` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `subject` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `content` TEXT NOT NULL COLLATE utf8mb4_general_ci,
    `minute` INT(10) NOT NULL,
    `start_date` date NOT NULL,
    `end_date` date NOT NULL,
    `open_fl` tinyint NOT NULL,
    `closing_comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_survey_user` (`user_id`),
    CONSTRAINT `FK_survey_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `top_survey` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `survey_id` bigint NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_top_survey_survey` (`survey_id`),
    CONSTRAINT `FK_top_survey_survey` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `section` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `survey_id` bigint NOT NULL,
    `title` VARCHAR(255) NULL DEFAULT NULL COLLATE utf8mb4_general_ci,
    `content` VARCHAR(255) NULL DEFAULT NULL COLLATE utf8mb4_general_ci,
    `next_section` bigint NOT NULL DEFAULT '0',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_section_survey` (`survey_id`),
    CONSTRAINT `FK_section_survey` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `question` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `section_id` bigint NOT NULL,
    `ask` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `oder` int NOT NULL,
    `multi_fl` tinyint NOT NULL,
    `dupl_fl` tinyint NOT NULL,
    `ess_fl` tinyint NOT NULL,
    `descrip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_question_section` (`section_id`),
    CONSTRAINT `FK_question_section` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `question_item` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `question_id` bigint NOT NULL,
    `content` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `oder` int NOT NULL,
    `next_section` bigint,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_question_item_question` (`question_id`),
    CONSTRAINT `FK_question_item_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `hashtag` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `value` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `value` (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `hashtag_survey` (
    `survey_id` bigint NOT NULL,
    `hashtag_id` bigint NOT NULL,
    UNIQUE KEY `survey_id_hashtag_id` (`survey_id`,`hashtag_id`),
    KEY `FK_hashtag_survey_hashtag` (`hashtag_id`),
    KEY `FK_hashtag_survey_survey` (`survey_id`) USING BTREE,
    CONSTRAINT `FK_hashtag_survey_hashtag` FOREIGN KEY (`hashtag_id`) REFERENCES `hashtag` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_hashtag_survey_survey` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `reply` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NULL,
    `survey_id` bigint NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_reply_user` (`user_id`),
    KEY `FK_reply_survey` (`survey_id`),
    CONSTRAINT `FK_reply_survey` FOREIGN KEY (`survey_id`) REFERENCES `survey` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_reply_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `answer` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `reply_id` bigint NOT NULL,
    `question_id` bigint NOT NULL,
    `question_item_id` bigint NULL,
    `response` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '',
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_dt` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `FK_answer_reply` (`reply_id`),
    KEY `FK_answer_question` (`question_id`),
    KEY `FK_answer_question_item` (`question_item_id`),
    CONSTRAINT `FK_answer_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_answer_question_item` FOREIGN KEY (`question_item_id`) REFERENCES `question_item` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `FK_answer_reply` FOREIGN KEY (`reply_id`) REFERENCES `reply` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;