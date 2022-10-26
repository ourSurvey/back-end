package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.*;
import com.oursurvey.repo.hashtagsurvey.HashtagSurveyRepo;
import com.oursurvey.service.answer.AnswerService;
import com.oursurvey.util.JsonUtil;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
class SurveyServiceImplTest {
    @Autowired
    SurveyService service;
    @Autowired
    HashtagSurveyRepo repo;
    @Autowired
    AnswerService answerService;
    @Autowired
    JsonUtil jsonUtil;

    @Test
    void test1111() {
        List<SurveyDto.MyList> list = service.findByUserId(11L);

        LocalDate now = LocalDate.now();

        list.forEach(e -> {
            if (now.isBefore(e.getStartDate())) {
                e.setStatus(-1);
            } else if(now.isAfter(e.getEndDate())) {
                e.setStatus(1);
            } else {
                e.setStatus(0);
            }
            System.out.println("e = " + e);
        });
    }

    @Test
    void test2() {
        Optional<SurveyDto.Detail> opt = service.findById("SUVY220911A003");

        ArrayList<QuestionInfo> questionInfoList = new ArrayList<>();

        SurveyDto.Detail surveyDetail = opt.get();
        List<SectionDto.Detail> sectionList = surveyDetail.getSectionList();
        sectionList.forEach(e -> {
            List<QuestionDto.Detail> questionList = e.getQuestionList();
            questionList.forEach(f -> {
                QuestionInfo info = new QuestionInfo(f.getId(), f.getMultiFl());
                if (f.getQuestionItemList().size() > 0) {
                    f.getQuestionItemList().forEach(ee -> {
                        info.addToMap(ee.getId(), 0);
                    });
                }

                questionInfoList.add(info);
            });
        });

        List<AnswerDto.Base> answerList = answerService.findByQuestionIds(questionInfoList.stream().map(QuestionInfo::getId).toList());
        answerList.forEach(e -> {
            QuestionInfo qInfo = questionInfoList.stream().filter(q -> q.getId().equals(e.getQuestionId())).findFirst().get();
            HashMap<String, Integer> multiMap = qInfo.getMultiMap();


            if (qInfo.multiFl.equals(1)) {
                if (StringUtils.hasText(e.getResponse())) {
                    JSONArray responseArray = new JSONArray(e.getResponse());
                    for (Object o : responseArray) {
                        multiMap.replace(o.toString(), multiMap.get(o.toString()) + 1);
                        qInfo.plusAnswerCount();
                    }
                } else {
                    if (!multiMap.containsKey("noreply")) {
                        multiMap.put("noreply", 1);
                    } else {
                        multiMap.replace("noreply", (multiMap.get("noreply")) + 1);
                    }
                    qInfo.plusAnswerCount();
                }
            } else {
                if (StringUtils.hasText(e.getResponse())) {
                    qInfo.addToList(e.getResponse());
                } else {
                    qInfo.addToList("noreply");
                }
            }
        });

        for (QuestionInfo q : questionInfoList) {
            if (q.getMultiFl().equals(1) && q.getMultiAnswerCount() > 0) {
                for (Map.Entry<String, Integer> set : q.getMultiMap().entrySet()) {
                    q.getMultiMap().replace(set.getKey(), Math.round((float) set.getValue() / q.getMultiAnswerCount() * 100));
                }
            }
        }
    }

    public static class QuestionInfo {
        private String id;
        private Integer multiFl;
        private Integer multiAnswerCount =0;
        private HashMap<String, Integer> multiMap = new HashMap<>();
        private List<String> subjectiveList = new ArrayList<>();

        public QuestionInfo(String id, Integer multiFl) {
            this.id = id;
            this.multiFl = multiFl;
        }

        public String getId() {
            return id;
        }

        public Integer getMultiFl() {
            return multiFl;
        }

        public Integer getMultiAnswerCount() {
            return multiAnswerCount;
        }

        public HashMap<String, Integer> getMultiMap() {
            return multiMap;
        }

        public List<String> getSubjectiveList() {
            return subjectiveList;
        }

        public void plusAnswerCount() {
            this.multiAnswerCount++;
        }

        public void addToMap(String key, Integer value) {
            this.multiMap.put(key, value);
        }

        public void addToList(String value) {
            this.subjectiveList.add(value);
        }

        @Override
        public String toString() {
            return "QuestionInfo{" +
                    "id='" + id + '\'' +
                    ", multiFl=" + multiFl +
                    ", multiAnswerCount=" + multiAnswerCount +
                    ", multiMap=" + multiMap +
                    ", subjectiveList=" + subjectiveList +
                    '}';
        }
    }
}