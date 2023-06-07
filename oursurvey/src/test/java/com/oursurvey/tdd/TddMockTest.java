package com.oursurvey.tdd;

import antlr.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oursurvey.controller.AuthController;
import com.oursurvey.dto.AuthDto;
import com.oursurvey.dto.TokenDto;
import com.oursurvey.dto.repo.UserDto;
import com.oursurvey.entity.Grade;
import com.oursurvey.entity.Point;
import com.oursurvey.entity.User;
import com.oursurvey.repo.grade.GradeRepo;
import com.oursurvey.repo.point.PointRepo;
import com.oursurvey.repo.user.UserRepo;
import com.oursurvey.service.user.UserService;
import com.oursurvey.service.user.UserServiceImpl;
import com.oursurvey.util.JwtUtil;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class TddMockTest {
    @Nested
    class ServiceMock {
        @Spy
        @InjectMocks
        UserServiceImpl userService;

        @Mock
        UserRepo userRepo;
        @Mock
        GradeRepo gradeRepo;
        @Mock
        PointRepo pointRepo;

        @Test
        @DisplayName("회원가입 테스트")
        void 회원가입() {
            User mockUser = User.builder().id(22L).email("email").nickname("nickname").pwd("pwd").build();
            Grade mockGrade = Grade.builder().id(1L).pivot(0).name("firstGrade").build();

            // given & stubing
            given(gradeRepo.getFirstGrade()).willReturn(mockGrade);
            given(userRepo.save(any())).willReturn(mockUser);

            // when
            Long index = userService.create(UserDto.Create.builder().build());

            // then
            then(gradeRepo).should(only()).getFirstGrade();
            then(userRepo).should(only()).save(any());
            then(pointRepo).should(only()).save(any());

            assertThat(index).isEqualTo(22L);
        }

    }
}

