package com.oursurvey.dto.repo;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionDto {
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Create {
        private String ask;
        private String descrip;
        private Integer multiFl;
        private Integer essFl;
        private Integer dupFl;
        private Integer randomShowFl;
        private Integer nextFl;
        private Integer oder;
        private List<QuestionItemDto.Create> questionItemList;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private String id;
        private String ask;
        private String descrip;
        private Integer multiFl;
        private Integer essFl;
        private Integer dupFl;
        private Integer randomShowFl;
        private Integer nextFl;
        private Integer oder;
        private List<QuestionItemDto.Detail> questionItemList;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Summary {
        private String id;
        private Boolean noReply=true;
        private Integer multiFl;
        private Integer answerCount =0;
        private final HashMap<String, Integer> multiMap = new HashMap<>();
        private final List<String> subjectiveList = new ArrayList<>();

        public Summary(String id, Integer multiFl) {
            this.id = id;
            this.multiFl = multiFl;
        }

        public void setNoReplyFalse() {
            this.noReply = false;
        }

        public void plusAnswerCount() {
            this.answerCount++;
        }

        public void addToMap(String key, Integer value) {
            this.multiMap.put(key, value);
        }

        public void addToList(String value) {
            this.subjectiveList.add(value);
        }

    }
}
