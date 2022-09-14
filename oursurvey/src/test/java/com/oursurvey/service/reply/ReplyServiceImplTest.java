package com.oursurvey.service.reply;

import com.oursurvey.entity.Reply;
import com.oursurvey.repo.reply.ReplyRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceImplTest {
    @Autowired
    ReplyRepo repo;
}