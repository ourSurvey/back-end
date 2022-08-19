package com.oursurvey.repo.reply;

import com.oursurvey.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepo extends JpaRepository<Reply, Long>, ReplyRepoCustom {
}
